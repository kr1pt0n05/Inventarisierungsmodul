import { Component, inject, input, model, output, signal, WritableSignal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatDialog } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { CardComponent } from '../../components/card/card.component';
import { CommentsEditorComponent } from "../../components/comments-editor/comments-editor.component";
import { DialogComponent, DialogData } from '../../components/dialog/dialog.component';
import { ItemEditorComponent } from '../../components/item-editor/item-editor.component';
import { TagsEditorComponent } from "../../components/tags-editor/tags-editor.component";
import { Article } from '../../models/Article';
import { Comment } from '../../models/comment';
import { InventoryItem, inventoryItemDisplayNames, inventoryItemFromArticle } from '../../models/inventory-item';
import { ArticleId, fixSingleArticleString } from '../../models/Order';
import { Tag } from '../../models/tag';
import { InventoriesService } from '../../services/inventories.service';
import { OrderService } from '../../services/order.service';
import { TagsService } from '../../services/tags.service';


/**
 * InventorizationComponent
 *
 * This Angular component provides an interface for creating or editing an inventory item,
 * including management of associated comments and integration with order articles.
 *
 * Main Features:
 * - Loads and displays an inventory item for editing, or provides an empty form for new items.
 * - Handles saving of inventory item changes and creation of new inventory items.
 * - Manages comments: fetches, adds, and deletes comments for the inventory item.
 * - Handles navigation after saving, depending on the context (new item, extension, or order).
 * - Supports pre-filling the form with article data for new items.
 *
 * Inputs:
 * - isNewInventorization: Indicates whether a new item is being inventorized (boolean).
 * - inventoryItem: The inventory item to be edited (InventoryItem).
 * - order_id, article_id: Optional IDs for order/article context.
 *
 * Outputs:
 * - onInventorization: Emits the saved inventory item after saving.
 *
 * Signals:
 * - editableInventoryItem: Mutable copy of the inventory item for editing.
 * - savedComments: List of persisted comments.
 * - newComments: List of newly added (unsaved) comments.
 * - deletedComments: List of comments marked for deletion.
 * - disabledInputs: Array of input field names that should be disabled.
 * - itemArticles, extensionArticles: Lists of article identifiers for batch processing.
 *
 * Methods:
 * - ngOnChanges(): Initializes the editable item and loads comments if an item is present.
 * - saveInventorization(): Saves changes to the inventory item (creates or updates) and handles comment changes.
 * - handleCommentChanges(): Processes new and deleted comments for the current item.
 * - _fetchComments(id): Loads comments for the current inventory item from the backend.
 * - _handleNewComments(id): Persists new comments to the backend and updates local state.
 * - _handleDeletedComments(id): Deletes marked comments from the backend and updates local state.
 * - _saveNewInventorization(): Creates a new inventory item and saves comments.
 * - _saveExistingInventorization(): Updates an existing inventory item and saves comments.
 * - _onInventorization(item): Updates local state, emits the event, and navigates based on context.
 * - _setArticleAsInventoryItem(articleStrings): Sets the current article as the inventory item based on provided article strings.
 * - _resetComments(): Clears all comment signals.
 * - _updateImportedArticle(): Updates the imported article after inventorization.
 * - _getItemChanges(): Computes and returns the changed fields of the inventory item.
 *
 * Usage Examples:
 * - New Inventorization with Empty Form:
 *   <app-inventorization
 *     [isNewInventorization]="true"
 *     (onInventorization)="handleInventorization($event)">
 *   </app-inventorization>
 *
 * - New Inventorization with Pre-filled Data:
 *   <app-inventorization
 *     [isNewInventorization]="true"
 *     [inventoryItem]="newItem"
 *     (onInventorization)="handleInventorization($event)">
 *   </app-inventorization>
 *
 * - Editing Existing Item:
 *   <app-inventorization
 *     [inventoryItem]="existingItem"
 *     (onInventorization)="handleInventorization($event)">
 *   </app-inventorization>
 *
 * Dependencies:
 * - Angular Material modules, ReactiveFormsModule.
 * - CommentsEditorComponent, InventoryItemEditorComponent, CardComponent.
 * - InventoriesService for backend integration.
 * - Comment and InventoryItem models.
 */
@Component({
  selector: 'app-inventorization',
  imports: [
    MatButtonModule,
    MatDividerModule,
    MatExpansionModule,
    MatChipsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    CommentsEditorComponent,
    ItemEditorComponent,
    TagsEditorComponent,
    CardComponent
  ],
  templateUrl: './inventorization.component.html',
  styleUrl: './inventorization.component.css'
})
export class InventorizationComponent {
  /**
   * Input signal indicating whether a new inventory item is being inventorized.
   * Defaults to false, meaning the component is in edit mode for an existing item.
   */
  isNew = model<boolean>(false);
  /**
   * Input signal for the inventory item to be edited (or undefined for new items).
   */
  inventoryItem = input<InventoryItem>({} as InventoryItem);
  itemColumns = inventoryItemDisplayNames;
  changes = signal<Partial<InventoryItem>>({} as InventoryItem);

  /**
   * Output event emitter, triggered when inventorization is saved.
   */
  onInventorization = output<InventoryItem>();


  /**
   * Signal holding a mutable copy of the inventory item for editing.
   */
  editableInventoryItem = signal<InventoryItem>({} as InventoryItem);
  isEditedValid = signal<boolean>(false);
  /**
   * Signal holding an array of input field names that should be disabled.
   */
  disabledInputs = signal<string[]>([]);

  /**
   * Signal holding the list of comments that have been persisted (saved).
   */
  savedComments = signal([] as Comment[]);
  /**
   * Signal holding the list of newly added (unsaved) comments.
   */
  newComments = signal([] as Comment[]);
  /**
   * Signal holding the list of comments marked for deletion.
   */
  deletedComments = signal([] as Comment[]);

  tags = signal<Tag[]>([]); // Holds the tags for the inventory item
  newTags = signal<string[]>([]); // Holds the new tags added by the user
  availableTags = signal<Tag[]>([]); // Holds the available tags fetched from the backend

  itemArticles = signal<string[]>([]);
  extensionArticles = signal<string[]>([]);

  currentArticleId: ArticleId = {} as ArticleId;



  private readonly _snackBar = inject(MatSnackBar);

  constructor(private readonly inventoriesService: InventoriesService,
    private readonly orderService: OrderService,
    private readonly tagsService: TagsService,
    private readonly router: Router,
    route: ActivatedRoute,
    public dialog: MatDialog) {
    route.queryParams.subscribe(val => {
      if (val['extensionArticles']) {
        this.extensionArticles.set(fixSingleArticleString([...val['extensionArticles']]));
      } else {
        this.extensionArticles.set([]);
      }

      if (val['itemArticles']) {
        this.itemArticles.set(fixSingleArticleString([...val['itemArticles']]));
      } else {
        this.itemArticles.set([]);
      }

      if (this.itemArticles().length > 0) {
        this._setArticleAsInventoryItem(this.itemArticles);
        this._resetComments();

      } else {
        this.currentArticleId = {} as ArticleId;
        this.editableInventoryItem.set({} as InventoryItem);
      }

    });
  }

  /**
   * Initializes the editable inventory item and loads comments if an item is present.
   * - For editing: disables the 'id' field and loads comments for the item.
   * - For new items: disables the 'created_at' field and resets all comment signals.
   */
  ngOnChanges() {
    this.editableInventoryItem.set({ ...this.inventoryItem() });
    this.tags.set(this.editableInventoryItem().tags ?? []);

    if (!this.isNew()) {
      this.disabledInputs.set(['id', 'created_at']);
      this._fetchComments(this.editableInventoryItem().id);
    } else {
      this.disabledInputs.set(['created_at']);
      this._resetComments();
    }

    this.tagsService.getTags().subscribe({
      next: (tags) => {
        this.availableTags.set([...tags.content]);
      },
      error: (error) => {
        this._notify('Fehler beim Laden der verfügbaren Tags', 'error', error);
      }
    });
  }

  /**
   * Saves the inventory item and associated comments.
   * - If creating a new item, attempts to create it and then handle comments.
   * - If editing and changes exist, updates the item and handles comments.
   * - If no changes, only processes comments and emits the event.
   * Emits the onInventorization event after completion.
   */
  saveInventorization() {
    if (this.isNew()) {
      this.orderService.getArticleById(this.currentArticleId.articleId).subscribe({
        next: (article) => {
          if (article.is_inventoried) {
            this._notify('Artikel ist bereits inventarisiert, ein neuer Inventargegenstand kann nicht erstellt werden.', 'error');
            return;
          }
          this._saveNewInventorization();
        },
        error: (error) => {
          this._saveNewInventorization();
        }
      });
    } else if (Object.keys(this.changes()).length > 0) {
      this._saveExistingInventorization();
    } else {
      this.handleCommentChanges();
      this._onInventorization(this.editableInventoryItem());
    }
  }

  /**
   * Opens a dialog to confirm deletion of the inventory item.
   * If confirmed, deletes the item and navigates back to the inventory list.
   */
  openDeleteDialog() {
    const data = {
      title: 'Gegenstand wirklich löschen?',
      description: 'Der Gegenstand wird aus dem Inventar entfernt und kann nicht wiederhergestellt werden.',
      cancelButtonText: 'Abbrechen',
      confirmButtonText: 'Löschen'
    };

    this._handleDialog(data, (result) => {
      if (result) {
        const id = this.editableInventoryItem().id;
        this.inventoriesService.deleteInventoryById(id).subscribe({
          next: () => {
            this._notify('Inventargegenstand erfolgreich gelöscht', 'success');
            this.router.navigate(['/inventory']);
          },
          error: (error) => {
            this._notify('Fehler beim Deinventarisieren des Inventargegenstands', 'error', error);
          }
        });
      }
    });
  }

  /**
   * Opens a confirmation dialog for deinventorization.
   * If confirmed, deinventorizes the inventory item and navigates to its detail page.
   */
  openDeinventorizationDialog() {
    const data = {
      title: 'Gegenstand wirklich deinventarisieren?',
      description: 'Der Gegenstand wird archiviert und kann nicht wiederhergestellt werden.',
      cancelButtonText: 'Abbrechen',
      confirmButtonText: 'Deinventarisieren'
    };

    this._handleDialog(data, (result) => {
      if (result) {
        const id = this.editableInventoryItem().id;
        this.inventoriesService.deinventorizeInventoryById(id).subscribe({
          next: (item) => {
            this.handleCommentChanges();
            this._notify('Inventargegenstand erfolgreich deinventarisiert', 'success');
            this.router.navigate(['/inventory/', id]);
          },
          error: (error) => {
            this._notify('Fehler beim Deinventarisieren des Inventargegenstands', 'error', error);
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

  /**
   * Processes new and deleted comments for the current inventory item.
   * Fetches the latest comments after all changes are processed.
   * If the item does not exist, logs an error.
   */
  handleCommentChanges() {
    if (this.editableInventoryItem().id) {
      const id = this.editableInventoryItem().id;
      this.inventoriesService.getInventoryById(id).subscribe({
        next: () => {
          this._handleDeletedComments(id);
          this._handleNewComments(id);
          this._fetchComments(id);
        },
        error: (error) => {
          this._notify('Inventargegenstand nicht gefunden, Kommentare können nicht verarbeitet werden.', 'error', error);
        }
      });
    }
  }

  /**
   * Loads all comments for the given inventory item from the backend.
   * Updates the savedComments signal with the fetched comments.
   * @param id The inventory item's ID.
   * @private
   */
  private _fetchComments(id: number) {
    this.inventoriesService.getCommentsForId(id).subscribe({
      next: (comments) => {
        this.savedComments.update(() => comments);
      },
      error: (error) => {
        this._notify('Fehler beim Laden der Kommentare', 'error', error);
      }
    });
  }

  /**
   * Adds all new (unsaved) comments to the backend for the given inventory item.
   * On success, moves the comment from newComments to savedComments.
   * @param id The inventory item's ID.
   * @private
   */
  private _handleNewComments(id: number) {
    for (const comment of this.newComments()) {
      this.inventoriesService.addCommentToId(id, comment).subscribe({
        next: (savedComment) => {
          this.savedComments.update(currentComments => [...currentComments, savedComment]);
          this.newComments.update(currentNewComments => currentNewComments.filter(c => c !== comment));
        },
        error: (error) => {
          this._notify('Fehler beim Hinzufügen des Kommentars', 'error', error);
        }
      });
    }
  }

  /**
   * Deletes all comments marked for deletion from the backend for the given inventory item.
   * On success, removes the comment from both deletedComments and savedComments.
   * @param id The inventory item's ID.
   * @private
   */
  private _handleDeletedComments(id: number) {
    for (const comment of this.deletedComments()) {
      if (comment.id) {
        this.inventoriesService.deleteCommentFromId(id, comment.id).subscribe({
          next: () => {
            this.savedComments.update(currentComments => currentComments.filter(c => c.id !== comment.id));
            this.deletedComments.update(currentDeletedComments => currentDeletedComments.filter(c => c !== comment));
          },
          error: (error) => {
            this._notify('Fehler beim Löschen des Kommentars', 'error', error);
          }
        });
      }
    }
  }

  /**
   * Resets all comment-related signals (saved, new, and deleted comments) to empty arrays.
   * @private
   */
  private _resetComments() {
    this.savedComments.set([]);
    this.newComments.set([]);
    this.deletedComments.set([]);
  }

  /**
   * Attempts to create a new inventory item in the backend.
   * If the item already exists, logs an error.
   * On success, handles comments and navigates accordingly.
   * @private
   */
  private _saveNewInventorization() {
    this.inventoriesService.getInventoryById(this.editableInventoryItem().id).subscribe({
      next: () => {
        this._notify('Inventargegenstand existiert bereits, ein neuer kann nicht erstellt werden.', 'error');
      },
      error: () => {
        this.inventoriesService.addInventoryItem(this.editableInventoryItem()).subscribe({
          next: (newItem) => {
            this.handleCommentChanges();
            if (this.currentArticleId.orderId && this.currentArticleId.articleId) {
              this._updateImportedArticle();
            }
            this._notify('Inventargegenstand erfolgreich erstellt', 'success');
            this._onInventorization(newItem);
          },
          error: (error) => {
            this._notify('Fehler beim Erstellen des neuen Inventargegenstands', 'error', error);
          }
        });
      }
    });
  }

  /**
   * Attempts to update an existing inventory item in the backend.
   * If the item does not exist, logs an error.
   * On success, handles comments and navigates accordingly.
   * @private
   */
  private _saveExistingInventorization() {
    this.inventoriesService.getInventoryById(this.editableInventoryItem().id).subscribe({
      next: () => {
        this.handleCommentChanges();
        this.inventoriesService.updateInventoryById(this.editableInventoryItem().id, this.changes()).subscribe({
          next: (updatedItem) => {
            this._notify('Inventargegenstand erfolgreich aktualisiert', 'success');
            this._onInventorization(updatedItem);
          },
          error: (error) => {
            this._notify('Fehler beim Aktualisieren des Inventargegenstands', 'error', error);
          }
        });
      },
      error: () => {
        this._notify('Inventargegenstand existiert nicht, Aktualisierung nicht möglich.', 'error');
      }
    });
  }

  /**
   * Updates local state and emits the onInventorization event after saving.
   * Navigates to the appropriate page depending on context:
   * - If there are more item articles, navigates to '/new' for the next.
   * - If there are extension articles, navigates to '/new-extension'.
   * - If in order context, navigates to '/orders'.
   * - Otherwise, navigates to the detail page of the saved inventory item.
   * @param inventoryItem The saved inventory item.
   * @private
   */
  private _onInventorization(inventoryItem: InventoryItem) {
    this._createNewTags();
    this.editableInventoryItem.set(inventoryItem);
    this.tags.set(inventoryItem.tags ?? []);
    this.onInventorization.emit(inventoryItem);

    if (this.itemArticles().length > 0) {
      this.router.navigate(['/new'], { queryParams: { itemArticles: [...this.itemArticles()], extensionArticles: this.extensionArticles() } });
    } else if (this.extensionArticles().length > 0) {
      this.router.navigate(['/new-extension'], { queryParams: { inventoryId: inventoryItem.id, extensionArticles: [...this.extensionArticles()] } });
    } else if (this.currentArticleId.orderId && this.currentArticleId.articleId) {
      this.router.navigate(['/orders'])
    } else {
      this.router.navigate(['/inventory/', inventoryItem.id]);
    }
  }

  /**
   * Creates new tags in the backend for each tag in the newTags signal.
   * On success, updates the tags signal and removes the tag from newTags.
   * Logs errors if any occur during the saving process.
   */
  private _createNewTags() {
    const newTags = this.newTags();
    if (newTags.length === 0) {
      this._setTagsOfItem();
      return;
    }
    const currentTags = this.tags();

    for (const tag of newTags) {
      this.tagsService.addTag({ name: tag } as Tag).subscribe({
        next: (savedTag) => {
          this.tags.update(t => [...currentTags, savedTag]);
          this.newTags.update(currentNewTags => currentNewTags.filter(t => t !== tag));

          if (this.newTags().length === 0) {
            this._setTagsOfItem();
          }
        },
        error: (error) => {
          this._notify('Fehler beim Speichern des Tags', 'error', error);
        }
      });
    }
  }

  /**
   * Updates the tags of the current inventory item.
   * If there are new tags, creates them first and then updates the item.
   * Logs success or error messages based on the operation outcome.
   */
  private _setTagsOfItem() {
    if (this.tags().length > 0) {
      this.inventoriesService.updateTagsOfId(this.editableInventoryItem().id, this.tags()).subscribe({
        next: (updatedItem) => {
          console.log('Tags updated successfully');
        },
        error: (error) => {
          this._notify('Fehler beim Aktualisieren der Tags', 'error', error);
        }
      });
    } else {
      this.inventoriesService.deleteTagsFromId(this.editableInventoryItem().id).subscribe({
        next: () => {
          console.log('Tags deleted successfully');
        },
        error: (error) => {
          this._notify('Fehler beim Löschen der Tags', 'error', error);
        }
      });
    }
  }

  /**
   * Sets the current article as the inventory item based on the provided article strings.
   * - Parses the first string as "orderId,articleId".
   * - Loads the article and sets it as the editable inventory item.
   * - Removes the processed article from the list.
   * @param articleStrings Signal containing the article strings in the format "orderId,articleId".
   * @private
   */
  private _setArticleAsInventoryItem(articleStrings: WritableSignal<string[]>) {
    [this.currentArticleId.orderId, this.currentArticleId.articleId] = articleStrings()[0].split(',').map(Number);
    this.orderService.getOrderArticleByIds(this.currentArticleId.orderId, this.currentArticleId.articleId).subscribe({
      next: (article) => {
        this.editableInventoryItem.set(inventoryItemFromArticle(article));
        this.tags.set(article.tags ?? []);
        articleStrings.update(articles => articles.slice(1)); // Remove the first article after setting it
      },
      error: (error) => {
        this._notify('Fehler beim Laden des Bestellartikels', 'error', error);
      }
    });
  }

  /**
   * Updates the imported article in the backend after inventorization.
   * Sets the article as inventoried and links it to the inventory item.
   * @private
   */
  private _updateImportedArticle() {
    const articleUpdates = {
      inventories_id: this.editableInventoryItem().id,
      is_inventoried: true,
      is_extension: false,
    } as unknown as Article;
    this.orderService.updateOrderArticle(this.currentArticleId.orderId, this.currentArticleId.articleId, articleUpdates).subscribe({
      next: (updatedArticle) => {
      },
      error: (error) => {
        this._notify('Fehler beim Aktualisieren des Artikels', 'error', error);
      }
    });
  }

  /**
   * Handles changes to the inventory item, updating the changes signal.
   * This method is called when an item is edited in the ItemEditorComponent.
   * @param item Partial inventory item with updated fields.
   */
  onItemChange(item: Partial<InventoryItem>) {
    this.changes.set(item);
  }

  /**
   * Shows a snackbar with a message and logs to the console.
   * @param message The message to show in the snackbar.
   * @param type 'success' or 'error' (affects styling).
   * @param error Optional error object to log.
   */
  private _notify(message: string, type: 'success' | 'error' | 'info', error?: any) {
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
}
