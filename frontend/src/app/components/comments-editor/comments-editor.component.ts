import { Component, input } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { Comment } from '../../models/comment';
import { InventoriesService } from '../../services/inventories.service';
import { CardComponent } from "../card/card.component";
import { DynamicListComponent } from "../dynamic-list/dynamic-list.component";

@Component({
  selector: 'app-comments-editor',
  imports: [
    CardComponent,
    DynamicListComponent,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
  ],
  templateUrl: './comments-editor.component.html',
  styleUrl: './comments-editor.component.css'
})
export class CommentsEditorComponent {
  constructor(private readonly inventoriesService: InventoriesService) { }

  inventoryId = input.required<number>();
  comments: Comment[] = [];

  commentsColumns = new Map<string, string>([
    ['description', 'Kommentar'],
    ['author', 'Hinzugefügt von'],
    ['createdAt', 'Hinzugefügt am']
  ]);

  newCommentFormControl = new FormControl('');

  ngOnInit(): void {
    this.inventoriesService.getCommentsForId(this.inventoryId()).subscribe((comments: Comment[]) => {
      this.comments = comments;
    });
  }

  getCommentDescriptions(): object[] {
    return this.comments.map(comment => { return { comment: comment.description } });
  }

  addComment(): void {
    const description = this.newCommentFormControl.value;
    if (description) {
      this.inventoriesService.addCommentToId(this.inventoryId(), description)
        .subscribe({
          next: (comment: Comment) => {
            this.comments.push(comment);
            this.newCommentFormControl.setValue('');
          },
          error: (error) => {
            // TODO: Proper error handling with in app user feedback
            console.error('Error adding comment:', error);
          }
        });
    }
  }

}
