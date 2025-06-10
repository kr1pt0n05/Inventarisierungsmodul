import { Component, computed, model, output } from '@angular/core';
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

  unchangedComments = model<Comment[]>([]);
  newComments = model<Comment[]>([]);
  deletedComments = model<Comment[]>([]);

  onChanges = output();

  displayedComments = computed(() => [...this.unchangedComments(), ...this.newComments()]);
  commentsColumns = new Map<string, string>([
    ['description', 'Kommentar'],
    ['author', 'Hinzugefügt von'],
    ['createdAt', 'Hinzugefügt am']
  ]);

  newCommentFormControl = new FormControl('');


  addComment(): void {
    const description = this.newCommentFormControl.value;
    if (description) {
      const newComment: Comment = {
        description,
        author: 'User', // Replace with actual user data
        createdAt: new Date().toLocaleString("de-De", { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit' }).replace(',', '')
      };

      this.newComments.update((currentComments) => [...currentComments, newComment]);
      this.onChanges.emit();
    }
  }

}
