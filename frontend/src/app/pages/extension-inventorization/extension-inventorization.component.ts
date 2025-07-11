import { Component, input, model, signal, WritableSignal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { localizePrice } from '../../app.component';
import { CardComponent } from '../../components/card/card.component';
import { DialogComponent, DialogData } from '../../components/dialog/dialog.component';
import { InventoryListComponent } from '../../components/inventory-list/inventory-list.component';
import { ItemEditorComponent } from '../../components/item-editor/item-editor.component';
import { Article } from '../../models/Article';
import { Extension, extensionDisplayNames, extensionItemFromArticle } from '../../models/extension';
import { InventoryItem, inventoryItemDisplayNames } from '../../models/inventory-item';
import { ArticleId, fixSingleArticleString } from '../../models/Order';
import { InventoriesService } from '../../services/inventories.service';
import { OrderService } from '../../services/order.service';
@Component({
  selector: 'app-extension-inventorization',
  imports: [
    CardComponent,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatInputModule,
    MatAutocompleteModule,
    InventoryListComponent,
    MatDividerModule,
    MatButtonModule,
    MatInputModule,
    ItemEditorComponent
  ],
  templateUrl: './extension-inventorization.component.html',
  styleUrl: './extension-inventorization.component.css'
})
export class ExtensionInventorizationComponent {
  /**
   * Indicates if the editor is in "new extension" mode.
   */
  isNew = model<boolean>(false);
  /**
   * Holds the current extension being edited.
   */
  editableExtension = signal<Extension>({} as Extension);
  itemColumns = extensionDisplayNames;
  extension = input<Extension | undefined>(undefined);
  changes = signal<Partial<Extension>>({} as Partial<Extension>);
  /**
   * Holds the current inventoryId (if set).
   */
  inventoryId = model<number | undefined>(undefined);

  /**
   * List of disabled input field keys.
   */
  disabledInputs = signal<string[]>(['created_at']);
  /**
   * List of required input field keys.
   */
  requiredInputs = signal<string[]>(['company', 'price', 'description']);

  /**
   * Holds the list of extension article string IDs to process (format: "orderId,articleId").
   */
  extensionArticles = signal<string[]>([]);

  isValid = signal<boolean>(false);

  /**
   * Holds the current inventory item (if loaded by inventoryId).
   */
  inventoryItem = {} as InventoryItem;
  /**
   * Holds the current article ID being processed.
   */
  currentArticleId: ArticleId = {} as ArticleId;

  /**
   * Defines the fields to display in the editor and their labels (for inventory items).
   */
  inventoryItemColumns = inventoryItemDisplayNames;

  /**
   * Map of FormControl objects for each extension property.
   */
  formControls = new Map<string, FormControl>(
    Array.from(extensionDisplayNames.keys()).map(key => [key, new FormControl('')])
  );
  /**
   * FormGroup containing all form controls for validation and value tracking.
   */
  formGroup = new FormGroup(Object.fromEntries(
    Array.from(extensionDisplayNames.keys()).map(key => [key, new FormControl('')])
  ));


  constructor(
    private readonly inventoriesService: InventoriesService,
    private readonly orderService: OrderService,
    private readonly router: Router, route: ActivatedRoute,
    public dialog: MatDialog,
    private readonly _snackBar: MatSnackBar) {
    // Handles query params for extensionArticles and inventoryId, and triggers article loading if needed.
    route.queryParams.subscribe(val => {
      if (val['extensionArticles']) {
        this.extensionArticles.set(fixSingleArticleString([...val['extensionArticles']]));
      } else {
        this.extensionArticles.set([]);
      }

      if (val['inventoryId']) {
        this.inventoryId.set(Number(val['inventoryId']));
      } else {
        this.inventoryId.set(undefined);
      }

      if (this.extensionArticles().length > 0) {
        this._setArticleAsExtension(this.extensionArticles);
      }
    });
  }

  /**
   * Angular lifecycle hook.
   * Initializes form controls, sets up autocomplete, disables fields as specified,
   * and sets default values for new extensions.
   */
  ngOnInit() {
    if (!this.editableExtension()) {
      this.editableExtension.set({} as Extension);
    }
    if (this.extension() !== undefined) {
      this.editableExtension.set(this.extension()!);
    }
  }

  /**
   * Angular lifecycle hook.
   * Resets form controls and values when the inventory item changes.
   */
  ngOnChanges() {
    this._onChanges();
  }

  /**
   * Called when the user selects an inventory item by ID.
   * Updates the inventoryId and reloads the inventory item.
   */
  onSelectInventory(id: number) {
    this.inventoryId.set(id);
    this._onChanges();
  }

  /**
   * Resets the selected inventory item and clears the form controls.
   * Sets inventoryId to undefined, clears the inventoryItem,
   * and resets the inventoryIdControl value.
   * Also triggers the _onChanges method to update the form state.
   */
  resetSelectedInventory() {
    this.inventoryId.set(undefined);
    this.inventoryItem = {} as InventoryItem;
    this._onChanges();
  }

  /**
   * Called when the user selects an article from the autocomplete.
   * Sets the selected article as the extension and updates the form controls.
   * @param articleStrings Signal containing the article strings in the format "orderId,articleId".
   */
  onExtensionChange(extension: Partial<Extension>) {
    this.changes.set(extension);
  }

  /**
   * Called after inventorization is completed.
   * Updates the imported article in the backend and navigates to the next step.
   */
  onInventorization() {
    const currentId = this.inventoryId();
    if (currentId !== undefined && this.isValid()) {
      if (this.isNew()) {
        this.orderService.getArticleById(this.currentArticleId.articleId).subscribe({
          next: (article) => {
            if (article.is_inventoried) {
              this._notify('Artikel ist bereits inventarisiert, eine neue Erweiterung kann nicht erstellt werden.', 'error');
              return;
            }
            this._saveNewExtension(currentId);
          },
          error: (error) => {
            this._saveNewExtension(currentId);
          }
        });
      } else {
        this._saveExistingExtension();
      }
    } else {
      this._notify('Inventarisierung ist nicht gültig oder die Inventar-ID ist nicht gesetzt.', 'error');
    }
  }

  /**
   * Shows a snackbar with a message and logs to the console.
   * @param message The message to show in the snackbar.
   * @param type 'success' or 'error' (affects styling).
   * @param error Optional error object to log.
   */
  private _notify(message: string, type: 'success' | 'error', error?: any) {
    if (type === 'error') {
      console.error(message, error);
    } else {
      console.log(message);
    }
    this._snackBar.open(message, 'Close', {
      duration: 3000,
      panelClass: [`${type}-snackbar`]
    });
  }

  private _saveNewExtension(currentId: number) {
    this.inventoriesService.addExtensionToId(currentId, this.editableExtension()).subscribe({
      next: (extension) => {
        this._notify('Erweiterung erfolgreich hinzugefügt', 'success');
        if (this.currentArticleId.orderId !== undefined && this.currentArticleId.articleId !== undefined) {
          this._updateImportedArticle();
        }
        this._navigateOnInventorization();
      },
      error: (error) => {
        this._notify('Fehler beim Hinzufügen der Erweiterung', 'error', error);
      }
    });
  }

  private _saveExistingExtension() {
    if (this.inventoryId() !== undefined) {
      this.changes.set({ ...this.changes(), inventory_id: this.inventoryId() });
      this.inventoriesService.updateExtension(this.inventoryId()!, this.editableExtension().id!, this.changes()).subscribe({
        next: (extension) => {
          this._notify('Erweiterung erfolgreich aktualisiert', 'success');
          this._navigateOnInventorization();
        },
        error: (error) => {
          this._notify('Fehler beim Aktualisieren der Erweiterung', 'error', error);
        }
      });
    }
  }

  /**
   * Navigates to the next step based on the current state:
   * - If there are extension articles, navigates to '/new-extension' with query params.
   * - If an inventoryId is set, navigates to the inventory detail page.
   * - Otherwise, navigates to the orders page.
   * @private
   */
  private _navigateOnInventorization() {
    if (this.extensionArticles().length > 0) {
      this.router.navigate(['/new-extension'], { queryParams: { inventoryId: this.inventoryId(), extensionArticles: [...this.extensionArticles()] } });
    } else if (this.inventoryId() !== undefined) {
      this.router.navigate(['/inventory/', this.inventoryId()]);
    } else {
      this.router.navigate(['/orders'])
    }
  }

  onIsValidChange(isValid: boolean) {
    this.isValid.set(isValid);
  }

  /**
   * Handles changes when inventoryId or extension changes.
   * Loads the inventory item if inventoryId is set, updates form values,
   * disables specified fields, and resets form state.
   * @private
   */
  private _onChanges() {
    if (this.inventoryId() !== undefined) {
      this._fetchInventoryItemById(this.inventoryId()!);
    }

    if (this.disabledInputs() === undefined) {
      this.disabledInputs.set(['created_at']);
    }
  }

  /**
   * Loads the inventory item by its ID and updates the local inventoryItem property.
   * If loading fails, resets inventoryItem and inventoryId.
   * @param inventoryId The ID of the inventory item to load.
   * @private
   */
  private _fetchInventoryItemById(inventoryId: number) {
    this.inventoriesService.getInventoryById(inventoryId).subscribe({
      next: (inventory) => {
        this.inventoryItem = inventory;
      },
      error: (error) => {
        this.inventoryItem = {} as InventoryItem;
        this.inventoryId.set(undefined);
        this._notify('Fehler beim Laden des Inventargegenstands', 'error', error);
      }
    });
  }

  /**
   * Sets the current article as the extension based on the provided article strings.
   * - Parses the first string as "orderId,articleId".
   * - Loads the article and sets it as the extension.
   * - Removes the processed article from the list.
   * - Updates the form controls and state.
   * @param articleStrings Signal containing the article strings in the format "orderId,articleId".
   * @private
   */
  private _setArticleAsExtension(articleStrings: WritableSignal<string[]>) {
    [this.currentArticleId.orderId, this.currentArticleId.articleId] = articleStrings()[0].split(',').map(Number);
    this.orderService.getOrderArticleByIds(this.currentArticleId.orderId, this.currentArticleId.articleId).subscribe({
      next: (article) => {
        this.editableExtension.set(extensionItemFromArticle(article));
        articleStrings.update(articles => articles.slice(1)); // Remove the first article after setting it
        this._onChanges();
      },
      error: (error) => {
        this._notify('Fehler beim Laden des Bestellartikels', 'error', error);
      }
    });
  }

  /**
   * Updates the imported article in the backend after inventorization.
   * Sets the article as inventoried and as an extension, and links it to the inventory item.
   * @private
   */
  private _updateImportedArticle() {
    const articleUpdates = {
      inventories_id: this.inventoryId(),
      is_inventoried: true,
      is_extension: true,
    } as Partial<Article>;
    this.orderService.updateOrderArticle(this.currentArticleId.orderId, this.currentArticleId.articleId, articleUpdates).subscribe({
      next: (updatedArticle) => {
        console.log('Artikel erfolgreich aktualisiert');
      },
      error: (error) => {
        this._notify('Fehler beim Aktualisieren des Artikels', 'error', error);
      }
    });
  }

  /**
 * Opens a dialog to confirm deletion of the inventory item.
 * If confirmed, deletes the item and navigates back to the inventory list.
 */
  openDeleteDialog() {
    const data = {
      title: 'Erweiterung wirklich löschen?',
      description: 'Die Erweiterung wird aus dem Inventar entfernt und kann nicht wiederhergestellt werden.',
      cancelButtonText: 'Abbrechen',
      confirmButtonText: 'Löschen'
    };

    this._handleDialog(data, (result) => {
      if (result) {
        const id = this.inventoryId()!;
        const extensionId = this.editableExtension().id!;
        this.inventoriesService.deleteExtensionFromId(id, extensionId).subscribe({
          next: () => {
            this._notify('Erweiterung erfolgreich gelöscht', 'success');
            this.router.navigate(['/inventory', id]);
          },
          error: (error) => {
            this._notify('Fehler beim Entfernen der Erweiterung', 'error', error);
          }
        });
      }
    });
  }

  /**
   * Opens a dialog with the provided data and executes a callback with the result.
   * This is used for confirmation dialogs like deletion or deinventorization.
   * @param dialogData The data to be passed to the dialog.
   * @param callback The function to call with the dialog result.
   */
  private _handleDialog(dialogData: DialogData, callback: (result: any) => void) {
    const dialogRef = this.dialog.open(DialogComponent, { data: dialogData });

    dialogRef.afterClosed().subscribe(result => {
      callback(result);
    });
  }

  localizePrice(price: number | string): string {
    return localizePrice(price);
  }

}
