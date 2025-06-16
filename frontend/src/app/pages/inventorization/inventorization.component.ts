import { Component, input, model, output, signal, WritableSignal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { ActivatedRoute, Router } from '@angular/router';
import { CardComponent } from "../../components/card/card.component";
import { CommentsEditorComponent } from "../../components/comments-editor/comments-editor.component";
import { InventoryItemEditorComponent } from "../../components/inventory-item-editor/inventory-item-editor.component";
import { Comment } from '../../models/comment';
import { InventoryItem, inventoryItemFromArticle } from '../../models/inventory-item';
import { Article, ArticleId, fixSingleArticleString } from '../../models/Order';
import { InventoriesService } from '../../services/inventories.service';
import { OrderService } from '../../services/order.service';


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
    CardComponent,
    CommentsEditorComponent,
    InventoryItemEditorComponent,
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

  itemArticles = signal<string[]>([]);
  extensionArticles = signal<string[]>([]);

  currentArticleId: ArticleId = {} as ArticleId;


  constructor(private readonly inventoriesService: InventoriesService, private readonly orderService: OrderService, private readonly router: Router, route: ActivatedRoute) {
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

    if (!this.isNewInventorization()) {
      this.disabledInputs.set(['id']);
      this._fetchComments(this.editableInventoryItem().id);
    } else {
      this.disabledInputs.set(['created_at']);
      this._resetComments();
    }
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
      this._saveNewInventorization();
    } else if (Object.keys(this._getItemChanges()).length > 0) {
      this._saveExistingInventorization();
    } else {
      console.warn('No changes detected, skipping save.');
      this.handleCommentChanges();
      this._onInventorization(this.editableInventoryItem());
    }
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
          console.error('Inventory item not found:', error);
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
      next: (comments) => { this.savedComments.update(() => comments) },
      error: (error) => { console.error('Error fetching comments:', error); }
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
          console.error('Error adding comment:', error);
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
            console.error('Error deleting comment:', error);
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
        console.error('Inventory item already exists, cannot create a new one.');
      },

      error: () => {
        this.inventoriesService.addInventoryItem(this.editableInventoryItem()).subscribe({
          next: (newItem) => {
            this.handleCommentChanges();
            if (this.currentArticleId.orderId && this.currentArticleId.articleId) {
              this._updateImportedArticle();
            }
            this._onInventorization(newItem);
          },

          error: (error) => {
            console.error('Error creating new inventory item:', error);
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
        this.inventoriesService.updateInventoryById(this.editableInventoryItem().id, this._getItemChanges()).subscribe({
          next: (updatedItem) => {
            this._onInventorization(updatedItem);
          },

          error: (error) => {
            console.error('Error updating inventory item:', error);
          }
        });
      },

      error: () => {
        console.error('Inventory item does not exist, cannot update.');
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
    this.editableInventoryItem.set(inventoryItem);
    this.onInventorization.emit(inventoryItem);
    console.log('Inventorization completed:', inventoryItem);

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
        this.editableInventoryItem.set(inventoryItemFromArticle(article!));
        articleStrings.update(articles => articles.slice(1)); // Remove the first article after setting it
      },
      error: (error) => {
        console.error('Error fetching order article:', error);
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
        console.log('Article updated successfully:', updatedArticle);
      },
      error: (error) => {
        console.error('Error updating article:', error);
      }
    });
  }

  /**
   * Computes and returns the changed fields of the inventory item compared to the original.
   * Only fields with changed values are included in the returned object.
   * @returns {InventoryItem} An object containing only the changed fields.
   * @private
   */
  private _getItemChanges(): InventoryItem {
    const changes: InventoryItem = {} as InventoryItem;
    for (const [key, value] of Object.entries(this.editableInventoryItem())) {
      if (value !== this.inventoryItem()[key as keyof InventoryItem]) {
        changes[key as keyof InventoryItem] = value;
      }
    }
    return changes;
  }
}
