import { Component, EventEmitter, input, Output, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { Router } from '@angular/router';
import { CardComponent } from "../../components/card/card.component";
import { CommentsEditorComponent } from "../../components/comments-editor/comments-editor.component";
import { InventoryItemEditorComponent } from "../../components/inventory-item-editor/inventory-item-editor.component";
import { Comment } from '../../models/comment';
import { InventoryItem } from '../../models/inventory-item';
import { InventoriesService } from '../../services/inventories.service';


/**
 * InventorizationComponent
 *
 * This Angular component provides the main interface for viewing and editing an inventory item,
 * including its associated comments. It coordinates the editing of item details and the management
 * of comments (adding, deleting, and fetching).
 *
 * Features:
 * - Loads and displays an inventory item for editing.
 * - Handles saving of inventory item changes.
 * - Manages comments: fetches, adds, and deletes comments for the inventory item.
 * - Emits an event when inventorization is completed.
 *
 * Properties:
 * - inventoryItem: Input signal for the inventory item to be edited.
 * - editableInventoryItem: Signal holding a mutable copy of the inventory item for editing.
 * - savedComments: Signal holding the list of persisted comments.
 * - newComments: Signal holding the list of newly added (unsaved) comments.
 * - deletedComments: Signal holding the list of comments marked for deletion.
 * - onInventorization: Output event emitter, triggered when inventorization is saved.
 *
 * Methods:
 * - ngOnInit(): Initializes the editable item and loads comments if an item is present.
 * - saveInvetarisation(): Saves changes to the inventory item and handles comment changes.
 * - handleCommentChanges(): Processes new and deleted comments for the current item.
 * - _fetchComments(): Loads comments for the current inventory item from the backend.
 * - _handleNewComments(): Persists new comments to the backend and updates local state.
 * - _handleDeletedComments(): Deletes marked comments from the backend and updates local state.
 * - _getItemChanges(): Computes and returns the changed fields of the inventory item.
 *
 * Usage:
 * Place <app-inventorization> in your template. Bind the inventoryItem input and listen to the onInventorization output.
 * If a new inventory item should be created, pass an inventoryItem with no id.
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
    InventoryItemEditorComponent
  ],
  templateUrl: './inventorization.component.html',
  styleUrl: './inventorization.component.css'
})
export class InventorizationComponent {
  constructor(private readonly inventoriesService: InventoriesService, private readonly router: Router) { }

  isNewInventorization = input<boolean>(false);
  inventoryItem = input<InventoryItem>({} as InventoryItem);
  editableInventoryItem = signal<InventoryItem>({} as InventoryItem);
  /**
   * Map of input fields that should be disabled in the editor.
   * This is used to prevent editing of certain fields, such as 'id' for existing items.
   * 
   * Default is an empty map, meaning no fields are disabled initially.
   * For existing items, the 'id' field is set to true to disable it.
   */
  disabledInputs = signal<Map<string, boolean>>(new Map<string, boolean>());

  savedComments = signal([] as Comment[]);
  newComments = signal([] as Comment[]);
  deletedComments = signal([] as Comment[]);

  /**
   * Output event emitter, triggered when inventorization is saved.
   */
  @Output() onInventorization = new EventEmitter<InventoryItem>();

  /**
   * Initializes the editable inventory item and loads comments if an item is present.
   */
  ngOnInit() {
    this.editableInventoryItem.set({ ...this.inventoryItem() });

    if (!this.isNewInventorization()) {
      this.disabledInputs.set(new Map<string, boolean>([['id', true]]));
      this._fetchComments();
    }
  }

  /**
   * Saves changes to the inventory item and handles comment changes.
   * Emits the onInventorization event after saving.
   */
  saveInventorization() {
    if (this.isNewInventorization()) {
      this._saveNewInventorization();
    } else {
      if (Object.keys(this._getItemChanges()).length > 0) {
        this._saveExistingInventorization();
      } else {
        console.warn('No changes detected, skipping save.');
        this._onInventorization(this.editableInventoryItem());
      }
    }
  }

  /**
   * Processes new and deleted comments for the current inventory item.
   */
  handleCommentChanges() {
    if (this.editableInventoryItem().id) {
      this.inventoriesService.getInventoryById(this.editableInventoryItem().id).subscribe({
        next: () => {
          this._handleDeletedComments();
          this._handleNewComments();
          this._fetchComments();
        },
        error: (error) => {
          console.error('Inventory item not found:', error);
        }
      });
    }
  }

  /**
   * Loads comments for the current inventory item from the backend.
   * Updates the savedComments signal.
   * @private
   */
  private _fetchComments() {
    this.inventoriesService.getCommentsForId(this.editableInventoryItem()!.id).subscribe({
      next: (comments) => { this.savedComments.update(() => comments) },
      error: (error) => { console.error('Error fetching comments:', error); }
    });
  }

  /**
   * Persists new comments to the backend and updates local state.
   * Removes successfully saved comments from the newComments signal and adds them to savedComments.
   * @private
   */
  private _handleNewComments() {
    for (const comment of this.newComments()) {
      this.inventoriesService.addCommentToId(this.editableInventoryItem()!.id, comment).subscribe({
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
   * Deletes marked comments from the backend and updates local state.
   * Removes successfully deleted comments from the deletedComments and savedComments signals.
   * @private
   */
  private _handleDeletedComments() {
    for (const comment of this.deletedComments()) {
      if (comment.id) {
        this.inventoriesService.deleteCommentFromId(this.editableInventoryItem()!.id, comment.id).subscribe({
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

  private _saveNewInventorization() {
    this.inventoriesService.getInventoryById(this.editableInventoryItem()!.id).subscribe({
      next: () => {
        console.error('Inventory item already exists, cannot create a new one.');
      },

      error: () => {
        this.inventoriesService.addInventoryItem(this.editableInventoryItem()).subscribe({
          next: (newItem) => {
            this.handleCommentChanges();
            this._onInventorization(newItem);
          },

          error: (error) => {
            console.error('Error creating new inventory item:', error);
          }
        });
      }
    });
  }

  private _saveExistingInventorization() {
    this.inventoriesService.getInventoryById(this.inventoryItem()!.id).subscribe({
      next: () => {
        this.handleCommentChanges();
        this.inventoriesService.updateInventoryById(this.inventoryItem()!.id, this._getItemChanges()).subscribe({
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

  private _onInventorization(inventoryItem: InventoryItem) {
    this.editableInventoryItem.set(inventoryItem);
    this.onInventorization.emit(inventoryItem);
    console.log('Inventorization completed:', inventoryItem);
    this.router.navigate(['/inventory/', inventoryItem.id]);
  }

  /**
   * Computes and returns the changed fields of the inventory item compared to the original.
   * @returns {InventoryItem} An object containing only the changed fields.
   * @private
   */
  private _getItemChanges(): InventoryItem {
    const changes: InventoryItem = {} as InventoryItem;
    for (const [key, value] of Object.entries(this.editableInventoryItem())) {
      if (value !== this.inventoryItem()![key as keyof InventoryItem]) {
        changes[key as keyof InventoryItem] = value;
      }
    }
    console.log('Changes detected:', changes);
    return changes;
  }
}
