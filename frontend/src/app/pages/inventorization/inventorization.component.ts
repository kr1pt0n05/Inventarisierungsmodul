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
import { concat, EMPTY, finalize, forkJoin, map, mergeMap, Observable, tap } from 'rxjs';
import { CommentsEditorComponent } from "../../components/comments-editor/comments-editor.component";
import { DialogComponent, DialogData } from '../../components/dialog/dialog.component';
import { InventoryItemEditorComponent } from "../../components/inventory-item-editor/inventory-item-editor.component";
import { TagsEditorComponent } from "../../components/tags-editor/tags-editor.component";
import { Article } from '../../models/Article';
import { Comment } from '../../models/comment';
import { InventoryItem, inventoryItemFromArticle } from '../../models/inventory-item';
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
    InventoryItemEditorComponent,
    TagsEditorComponent,
  ],
  templateUrl: './inventorization.component.html',
  styleUrl: './inventorization.component.css'
})
export class InventorizationComponent {
  /**
   * Input signal indicating whether a new inventory item is being inventorized.
   * Defaults to false, meaning the component is in edit mode for an existing item.
   */
  isNewInventorization = model<boolean>(false);
  /**
   * Input signal for the inventory item to be edited (or undefined for new items).
   */
  inventoryItem = input<InventoryItem>({} as InventoryItem);

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

    if (!this.isNewInventorization()) {
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
    if (this.isNewInventorization()) {
      if (this.currentArticleId.articleId === undefined) {
        this._saveNewInventorization();
      } else {
        this.orderService.getArticleById(this.currentArticleId.articleId).subscribe({
          next: (article) => {
            if (article.is_inventoried) {
              this._notify('Artikel ist bereits inventarisiert, ein neuer Inventargegenstand kann nicht erstellt werden.', 'error');
            } else {
              this._saveNewInventorization();
            }
          },
          error: (error) => {
            this._notify('Der zu inventarisierende Artikel existiert nicht', 'error', error);
          }
        });
      }
    } else if (Object.keys(this._getItemChanges()).length > 0) {
      this._saveExistingInventorization();
    } else {
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
            this._notify('Inventargegenstand erfolgreich deinventarisiert', 'success');
            this._onInventorization(item);
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
   * Handles changes to comments for the current inventory item.
   * - Processes deleted comments and new comments.
   * - Fetches the latest comments from the backend.
   * @returns {Observable<InventoryItem | void[] | Comment | Comment[]>}
   */
  handleCommentChanges(): Observable<InventoryItem | void[] | Comment | Comment[]> {
    const id = this.editableInventoryItem().id;
    return concat(
      this._handleDeletedComments(id),
      this._handleNewComments(id),
      this._fetchComments(id)
    ).pipe(
      tap({
        error: (error) => {
          this._notify('Kommentare konnten nicht verarbeitet werden.', 'error', error);
        }
      })
    );
  }

  /**
   * Fetches comments for the current inventory item from the backend.
   * Updates the savedComments signal with the fetched comments.
   * Logs errors if any occur during the fetching process.
   * @param id The inventory item's ID.
   * @returns {Observable<Comment[]>} An observable that emits the fetched comments.
   * @private
   */
  private _fetchComments(id: number): Observable<Comment[]> {
    return this.inventoriesService.getCommentsForId(id).pipe(
      tap({
        next: (comments) => {
          this.savedComments.update(() => comments);
        },
        error: (error) => {
          this._notify('Fehler beim Laden der Kommentare', 'error', error);
        }
      })
    );
  }

  /**
   * Handles adding new comments to the backend for the given inventory item.
   * Returns an observable that completes when all new comments are saved.
   * On success, updates the savedComments and newComments signals.
   * Logs errors if any occur during the saving process.
   * @param id The inventory item's ID.
   * @returns {Observable<void>} An observable that completes when all new comments are saved.
   * @private
   */
  private _handleNewComments(id: number): Observable<Comment> {
    return concat(...this.newComments().map(comment =>
      this.inventoriesService.addCommentToId(id, comment).pipe(
        tap({
          next: savedComment => this._onCommentAdded(savedComment, comment),
          error: (error) => this._notify('Fehler beim Hinzufügen des Kommentars', 'error', error)
        })
      )
    ));
  }

  /**
   * Handles updating signals after a comment is added.
   * @param savedComment The comment that was saved.
   * @param originalComment The original comment object.
   * @private
   */
  private _onCommentAdded(savedComment: Comment, originalComment: Comment) {
    this.savedComments.update(currentComments => [...currentComments, { ...savedComment }]);
    this.newComments.update(currentNewComments => currentNewComments.filter(c => c !== originalComment));
  }

  /**
   * Deletes all comments marked for deletion from the backend for the given inventory item.
   * On success, removes the comment from both savedComments and deletedComments signals.
   * @param id The inventory item's ID.
   * @returns {Observable<void[]>} An observable that completes when all deletions are done.
   * @private
   */
  private _handleDeletedComments(id: number): Observable<void[]> {
    return forkJoin(this.deletedComments().map(comment =>
      this.inventoriesService.deleteCommentFromId(id, comment.id!).pipe(
        tap({
          next: () => this._onCommentDeleted(comment),
          error: (error) => this._notify('Fehler beim Löschen des Kommentars', 'error', error)
        })
      )
    ));
  }

  /**
   * Handles updating signals after a comment is deleted.
   * @param comment The comment that was deleted.
   * @private
   */
  private _onCommentDeleted(comment: Comment) {
    this.savedComments.update(currentComments => currentComments.filter(c => c.id !== comment.id));
    this.deletedComments.update(currentDeletedComments => currentDeletedComments.filter(c => c !== comment));
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
        forkJoin([this.inventoriesService.addInventoryItem(this.editableInventoryItem()).pipe(
          tap({
            next: (newItem) => {
              this._notify('Inventargegenstand erfolgreich erstellt', 'success');
              this._onInventorization(newItem);
            },
            error: (error) => {
              this._notify('Fehler beim Erstellen des neuen Inventargegenstands', 'error', error);
            }
          })
        ),
        (this.currentArticleId.orderId && this.currentArticleId.articleId) ? this._updateImportedArticle() : EMPTY
        ]).subscribe({
          next: ([newItem, _]) => this._onInventorization(newItem)
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
        this.inventoriesService.updateInventoryById(this.editableInventoryItem().id, this._getItemChanges()).subscribe({
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
   * Handles the inventorization process for the inventory item.
   * - Creates new tags if any are added.
   * - Updates the tags of the inventory item.
   * - Emits the updated inventory item and redirects based on the current state.
   * @param inventoryItem The inventory item being inventorized.
   * @private
   */
  private _onInventorization(inventoryItem: InventoryItem) {
    forkJoin([
      this._createNewTags(),
      this.handleCommentChanges()
    ]).pipe(
      finalize(() => {
        inventoryItem.tags = this.tags();
        this.editableInventoryItem.set(inventoryItem);
        this.onInventorization.emit(inventoryItem);
        this._redirectOnInventorization(inventoryItem);
      })
    ).subscribe();
  }

  /**
   * Redirects the user based on the current state after inventorization.
   * - If there are item articles, navigates to '/new' with itemArticles and extensionArticles as query params.
   * - If there are extension articles, navigates to '/new-extension' with inventoryId and extensionArticles as query params.
   * - If in order context, navigates to '/orders'.
   * - Otherwise, navigates to the detail page of the saved inventory item.
   * @param inventoryItem The saved inventory item.
   * @private
   */
  private _redirectOnInventorization(inventoryItem: InventoryItem) {
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
   * Creates new tags in the backend based on the newTags signal.
   * If there are no new tags, returns an empty observable.
   * On success, updates the tags signal with the newly created tags and clears newTags.
   * Logs errors if any occur during the creation process.
   * @returns {Observable<Tag[]>} An observable emitting the newly created tags.
   */
  private _createNewTags(): Observable<Tag[]> {
    const newTags = this.newTags();
    if (newTags.length === 0) {
      return this._setTagsOfItem();
    }
    const currentTags = this.tags();

    return this.tagsService.addTags(newTags).pipe(
      tap({
        next: (savedTags) => {
          this.tags.update(t => [...currentTags, ...savedTags]);
          console.log('Tags erstellt', this.tags());
          this.newTags.set([]);
        },
        error: (error) => {
          this._notify('Fehler beim Speichern der Tags', 'error', error);
        }
      }),
      mergeMap(() => this._setTagsOfItem())
    );
  }

  /**
   * Updates the tags of the current inventory item in the backend.
   * Returns an observable that emits the updated tags.
   * Logs errors if any occur during the update process.
   * @returns {Observable<Tag[]>} An observable emitting the updated tags.
   */
  private _setTagsOfItem(): Observable<Tag[]> {
    return this.inventoriesService.updateTagsOfId(this.editableInventoryItem().id, this.tags()).pipe(
      map(item => item.tags ?? []),
      tap({
        next: updatedTags => {
          this.tags.set(updatedTags);
          console.log('Tags aktualisiert', this.tags());
        },
        error: (error) => {
          this._notify('Fehler beim Aktualisieren der Tags', 'error', error);
        }
      })
    );
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
    } as Partial<Article>;
    return this.orderService.updateOrderArticle(this.currentArticleId.orderId, this.currentArticleId.articleId, articleUpdates).pipe(
      tap({
        error: (error) => {
          this._notify('Fehler beim Aktualisieren des Artikels', 'error', error);
        }
      })
    );
  }

  /**
   * Computes and returns the changed fields of the inventory item compared to the original.
   * Only fields with changed values are included in the returned object.
   * @returns {InventoryItem} An object containing only the changed fields.
   * @private
   */
  private _getItemChanges(): Partial<InventoryItem> {
    const changes: Partial<InventoryItem> = {} as InventoryItem;
    for (const [key, value] of Object.entries(this.editableInventoryItem())) {
      if (value !== this.inventoryItem()[key as keyof InventoryItem]) {
        changes[key as keyof InventoryItem] = value;
      }
    }
    return changes;
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
