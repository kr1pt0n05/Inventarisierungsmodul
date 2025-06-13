import { Component, computed, model, output } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatSortModule } from '@angular/material/sort';
import { MatCell, MatCellDef, MatColumnDef, MatHeaderCell, MatHeaderCellDef, MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef, MatTable, MatTableDataSource } from '@angular/material/table';
import { Comment } from '../../models/comment';
import { AuthenticationService } from '../../services/authentication.service';
import { CardComponent } from "../card/card.component";

/**
 * CommentsEditorComponent
 * 
 * This Angular component provides a UI for viewing, adding, and deleting comments.
 * It is designed to be used as part of an inventory management module.
 * 
 * Features:
 * - Displays a table of comments (existing and newly added, but not yet deleted).
 * - Allows users to add new comments via a form input.
 * - Allows users to mark comments for deletion.
 * - Tracks unsaved changes (new or deleted comments).
 * - Emits an event when comments are changed.
 * 
 * Properties:
 * - unchangedComments: Model holding the list of existing comments.
 * - newComments: Model holding the list of newly added comments (not yet saved).
 * - deletedComments: Model holding the list of comments marked for deletion.
 * - onChanges: Output event emitter, triggered when comments are changed.
 * - displayedComments: Computed property providing the data source for the comments table.
 * - commentsColumns: Map defining the columns to display in the table.
 * - newCommentFormControl: Form control for the new comment input field.
 * 
 * Methods:
 * - getColumnIds(): Returns the list of column IDs for the table.
 * - addComment(): Adds a new comment to the newComments model and emits a change event.
 * - deleteComment(comment): Marks a comment for deletion or removes it from newComments.
 * - hasUnsavedChanges(): Returns true if there are unsaved new or deleted comments.
 * 
 * Usage:
 * Place <app-comments-editor> in your template. Bind to its outputs and provide necessary services.
 * 
 * Dependencies:
 * - Angular Material Table, Form, and Button modules.
 * - Comment model.
 */

@Component({
  selector: 'app-comments-editor',
  imports: [
    CardComponent,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatTable,
    MatColumnDef,
    MatHeaderCell,
    MatCell,
    MatHeaderCellDef,
    MatCellDef,
    MatHeaderRow,
    MatRow,
    MatRowDef,
    MatHeaderRowDef,
    MatPaginatorModule,
    MatSortModule,

  ],
  templateUrl: './comments-editor.component.html',
  styleUrl: './comments-editor.component.css'
})
export class CommentsEditorComponent {
  constructor(private readonly authService: AuthenticationService) { }

  unchangedComments = model<Comment[]>([]);
  newComments = model<Comment[]>([]);
  deletedComments = model<Comment[]>([]);

  onChanges = output();

  displayedComments = computed(() => {
    return new MatTableDataSource([...this.unchangedComments(), ...this.newComments()])
  });
  commentsColumns = new Map<string, string>([
    ['description', 'Kommentar'],
    ['author', 'Hinzugefügt von'],
    ['createdAt', 'Hinzugefügt am'],
    ['delete', '']
  ]);

  newCommentFormControl = new FormControl('');


  /**
   * Returns the list of column IDs to be displayed in the comments table.
   * @returns {string[]} Array of column IDs.
   */
  getColumnIds(): string[] {
    return Array.from(this.commentsColumns.keys());
  }

  /**
   * Adds a new comment to the newComments model and emits a change event.
   * The comment's author is set to the current user (from AuthenticationService).
   * The creation date is set to the current date and time.
   * Resets the input form after adding.
   */
  addComment(): void {
    const description = this.newCommentFormControl.value;
    if (description) {
      const newComment: Comment = {
        description,
        author: this.authService.getUsername() ?? '-', // TODO: Replace with actual user data
        createdAt: new Date().toLocaleString("de-De",
          {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
          }).replace(',', '')
      };

      this.newComments.update((currentComments) => [...currentComments, newComment]);
      this.onChanges.emit();
      this.newCommentFormControl.setValue('');
    }
  }

  /**
   * Marks a comment for deletion.
   * If the comment has an ID, it is considered persisted and moved to deletedComments.
   * Otherwise, it is removed from newComments.
   * @param {Comment} comment - The comment to delete.
   */
  deleteComment(comment: Comment): void {
    console.log('Delete comment:', comment);
    if (comment.id) {
      this.deletedComments.update((currentDeletedComments) => [...currentDeletedComments, comment]);
      this.unchangedComments.update((currentSavedComments) => currentSavedComments.filter(c => c.id !== comment.id));
    } else {
      this.newComments.update((currentNewComments) => currentNewComments.filter(c => c !== comment));
    }
  }

  /**
   * Checks if there are any unsaved changes (new or deleted comments).
   * @returns {boolean} True if there are unsaved changes, false otherwise.
   */
  hasUnsavedChanges(): boolean {
    return this.newComments().length > 0 || this.deletedComments().length > 0;
  }

}
