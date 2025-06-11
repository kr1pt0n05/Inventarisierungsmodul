import { Component, EventEmitter, input, Output, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
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
  constructor(private readonly inventoriesService: InventoriesService) { }

  /**
   * Input signal for the inventory item to be edited.
   */
  inventoryItem = input<InventoryItem | undefined>(undefined);

  /**
   * Signal holding a mutable copy of the inventory item for editing.
   */
  editableInventoryItem = signal<InventoryItem>({} as InventoryItem);

  /**
   * Signal holding the list of persisted comments.
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

  /**
   * Output event emitter, triggered when inventorization is saved.
   */
  @Output() onInventorization = new EventEmitter<InventoryItem>();

  /**
   * Initializes the editable inventory item and loads comments if an item is present.
   */
  ngOnInit() {
    if (this.inventoryItem() !== undefined) {
      this.editableInventoryItem.set({ ...this.inventoryItem()! });
    }

    if (this.inventoryItem() && this.inventoryItem()!.id) {
      this._fetchComments();
    }
  }

  /**
   * Saves changes to the inventory item and handles comment changes.
   * Emits the onInventorization event after saving.
   */
  saveInventorization() {
    if (this.inventoryItem() && this.inventoryItem()!.id) {
      this._getItemChanges(); // For debugging purposes, log the changes
      this.inventoriesService.updateInventoryById(this.inventoryItem()!.id, this._getItemChanges()).subscribe({
        next: (updatedItem) => {
          this.editableInventoryItem.set(updatedItem);
          console.log('Inventory item updated successfully:', updatedItem);
        },
        error: (error) => {
          console.error('Error updating inventory item:', error);
        }
      });
      this.handleCommentChanges();
    } else {
      this.inventoriesService.addInventoryItem(this.editableInventoryItem());
      this.handleCommentChanges();
    }
    this.onInventorization.emit(this.editableInventoryItem());
  }

  /**
   * Processes new and deleted comments for the current inventory item.
   */
  handleCommentChanges() {
    if (this.inventoryItem() && this.inventoryItem()!.id) {
      this._handleDeletedComments();
      this._handleNewComments();
    }
  }

  /**
   * Loads comments for the current inventory item from the backend.
   * Updates the savedComments signal.
   * @private
   */
  private _fetchComments() {
    this.inventoriesService.getCommentsForId(this.inventoryItem()!.id).subscribe({
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
      this.inventoriesService.addCommentToId(this.inventoryItem()!.id, comment).subscribe({
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
        this.inventoriesService.deleteCommentFromId(this.inventoryItem()!.id, comment.id).subscribe({
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
