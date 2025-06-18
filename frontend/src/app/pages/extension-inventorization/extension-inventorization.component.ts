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
import { CardComponent } from '../../components/card/card.component';
import { DialogComponent, DialogData } from '../../components/dialog/dialog.component';
import { ExtensionEditorComponent } from "../../components/extension-editor/extension-editor.component";
import { InventoryListComponent } from '../../components/inventory-list/inventory-list.component';
import { Article } from '../../models/Article';
import { Extension, extensionDisplayNames, extensionItemFromArticle } from '../../models/extension';
import { InventoryItem, inventoryItemDisplayNames } from '../../models/inventory-item';
import { ArticleId, fixSingleArticleString } from '../../models/Order';
import { AuthenticationService } from '../../services/authentication.service';
import { CacheInventoryService } from '../../services/cache-inventory.service';
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
    ExtensionEditorComponent
  ],
  templateUrl: './extension-inventorization.component.html',
  styleUrl: './extension-inventorization.component.css'
})
export class ExtensionInventorizationComponent {
  /**
   * Indicates if the editor is in "new extension" mode.
   */
  isNewExtension = model<boolean>(false);
  /**
   * Holds the current extension being edited.
   */
  extension = signal<Extension>({} as Extension);
  inputExtension = input<Extension | undefined>(undefined);
  changes = signal<Partial<Extension>>({} as Partial<Extension>);
  /**
   * Holds the current inventoryId (if set).
   */
  inventoryId = model<number | undefined>(undefined);

  /**
   * List of disabled input field keys.
   */
  disabledInputs = signal<string[]>([]);
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
   * FormControl for the inventoryId selection.
   */
  inventoryIdControl = new FormControl('');
  /**
   * FormGroup containing all form controls for validation and value tracking.
   */
  formGroup = new FormGroup(Object.fromEntries(
    Array.from(extensionDisplayNames.keys()).map(key => [key, new FormControl('')])
  ));


  constructor(
    private readonly cache: CacheInventoryService,
    private readonly authService: AuthenticationService,
    private readonly inventoriesService: InventoriesService,
    private readonly orderService: OrderService,
    private readonly router: Router, route: ActivatedRoute,
    public dialog: MatDialog,
    private readonly _snackBar: MatSnackBar // <--- Add MatSnackBar injection
  ) {
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
    if (this.isNewExtension() === undefined) {
      this.isNewExtension.set(false);
    }
    if (!this.extension()) {
      this.extension.set({} as Extension);
    }
    if (this.inputExtension() !== undefined) {
      this.extension.set(this.inputExtension()!);
    }
    if (this.disabledInputs() === undefined) {
      this.disabledInputs.set(this.isNewExtension() ? ['created_at'] : []);
    }
    if (this.requiredInputs() === undefined) {
      this.requiredInputs.set(['company', 'price', 'description']);
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
  onSelectInventory(id: number | undefined = undefined) {
    if (id === undefined) {
      this.inventoryId.set(Number(this.inventoryIdControl.value));
    } else {
      this.inventoryId.set(id);
    }
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
    this.inventoryIdControl.setValue('');
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
      if (this.isNewExtension()) {
        this._saveNewExtension(currentId);
      } else {
        this._saveExistingExtension();
      }
    } else {
      this._notify('Inventorization is not valid or inventoryId is not set.', 'error');
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
    this.inventoriesService.addExtensionToId(currentId, this.extension()).subscribe({
      next: (extension) => {
        this._notify('Extension added successfully', 'success');
        if (this.currentArticleId.orderId !== undefined && this.currentArticleId.articleId !== undefined) {
          this._updateImportedArticle();
        }
        this._navigateOnInventorization();
      },
      error: (error) => {
        this._notify('Error adding extension', 'error', error);
      }
    });
  }

  private _saveExistingExtension() {
    if (this.inventoryId() !== undefined) {
      this.changes.set({ ...this.changes(), inventory_id: this.inventoryId() });
      this.inventoriesService.updateExtension(this.inventoryId()!, this.extension().id!, this.changes()).subscribe({
        next: (extension) => {
          this._notify('Extension updated successfully', 'success');
          this._navigateOnInventorization();
        },
        error: (error) => {
          this._notify('Error updating extension', 'error', error);
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

    if (this.isNewExtension()) {
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
        this._notify('Error fetching inventory item', 'error', error);
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
        this.extension.set(extensionItemFromArticle(article));
        articleStrings.update(articles => articles.slice(1)); // Remove the first article after setting it
        this._onChanges();
      },
      error: (error) => {
        this._notify('Error fetching order article', 'error', error);
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
    } as unknown as Article;
    this.orderService.updateOrderArticle(this.currentArticleId.orderId, this.currentArticleId.articleId, articleUpdates).subscribe({
      next: (updatedArticle) => {
        console.log('Article updated successfully');
      },
      error: (error) => {
        this._notify('Error updating article', 'error', error);
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
        const extensionId = this.extension().id!;
        this.inventoriesService.deleteExtensionFromId(id, extensionId).subscribe({
          next: () => {
            this._notify('Extension deleted successfully', 'success');
            this.router.navigate(['/inventory', id]);
          },
          error: (error) => {
            this._notify('Error deinventorizing inventory item', 'error', error);
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

}
