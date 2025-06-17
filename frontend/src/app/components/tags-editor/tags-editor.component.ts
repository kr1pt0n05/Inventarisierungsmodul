import { ChangeDetectorRef, Component, computed, input, model, signal, WritableSignal } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatAutocomplete, MatAutocompleteSelectedEvent, MatAutocompleteTrigger } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatChipGrid, MatChipInput, MatChipRemove, MatChipRow } from '@angular/material/chips';
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { MatOption } from '@angular/material/select';
import { Tag } from '../../models/tag';
import { CardComponent } from '../card/card.component';

@Component({
  selector: 'app-tags-editor',
  imports: [
    CardComponent,
    MatFormField,
    MatLabel,
    MatChipGrid,
    MatChipRow,
    MatChipInput,
    ReactiveFormsModule,
    MatIcon,
    MatChipRemove,
    MatAutocomplete,
    MatOption,
    MatAutocompleteTrigger,
    FormsModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './tags-editor.component.html',
  styleUrl: './tags-editor.component.css'
})
export class TagsEditorComponent {
  /**
   * Holds the currently selected tags (Tag objects).
   */
  tags = model<Tag[]>([]);

  /**
   * Holds new tag names added by the user that are not yet part of availableTags.
   */
  newTags = model<string[]>([]);

  /**
   * Input: List of available tags fetched from the backend.
   */
  availableTags = input<Tag[]>([]);

  /**
   * Signal holding the FormControl for the selected tag names (used for chips).
   */
  control = signal(new FormControl<string[]>([]));

  /**
   * FormControl for the user's text input in the autocomplete field.
   */
  userTextInput = new FormControl<string>('');

  /**
   * Signal reflecting the current value of userTextInput.
   */
  userTextInputSignal: WritableSignal<string> = signal('');

  /**
   * Computed property that returns all available tag names and new tags,
   * filtered by the user's current text input (case-insensitive).
   */
  selectedOptions = computed(() => {
    return this.availableTags().map(tag => tag.name).concat(this.newTags()).filter(tag =>
      tag.toLowerCase().includes(this.userTextInputSignal().toLowerCase() ?? '')
    );
  });

  constructor(private readonly changeDetectorRef: ChangeDetectorRef) { }

  /**
   * Subscribes to userTextInput changes and updates userTextInputSignal accordingly.
   */
  ngOnInit() {
    this.userTextInput.valueChanges.subscribe(value => {
      this.userTextInputSignal.set(value ?? '');
    });
  }

  /**
   * Updates the control value with the current tags and new tags
   * whenever the input properties change.
   */
  ngOnChanges() {
    console.log('ngOnChanges called');
    this.control().setValue(this.tags().map(tag => tag.name).concat(this.newTags()));
  }

  /**
   * Removes a tag (by name) from the selected tags and from newTags if present.
   * Triggered when the user clicks the remove icon on a chip.
   * @param element - The tag name to remove.
   */
  remove(element: string): void {
    this.control().setValue(this.control().value!.filter(v => v.toLowerCase() !== element.toLowerCase()));

    if (this.newTags().includes(element.toLowerCase())) {
      this.newTags.update(tags => tags.filter(tag => tag.toLowerCase() !== element.toLowerCase()));
    }

    this.tags.set(this.availableTags().filter(tag => this.control().value!.includes(tag.name)));
  }

  /**
   * Handles the addition of a tag via autocomplete selection or button click.
   * - If an existing tag is selected, toggles its selection.
   * - If a new tag is added via button, adds it to newTags and the selection.
   * Updates the tags property with the selected Tag objects.
   * @param event - The selection event from autocomplete or a MouseEvent from the add button.
   */
  addTag(event: MatAutocompleteSelectedEvent | MouseEvent): void {
    if (event instanceof MatAutocompleteSelectedEvent) {
      this._addExistingTags(event);
    } else {
      // If triggered by button click, add as new tag
      this._addNewTag();
    }

    // Update tags with Tag objects matching the selected names
    this.tags.set(this.availableTags().filter(tag => this.control().value!.includes(tag.name)));
  }

  /**
   * Handles the selection of an existing tag from the autocomplete dropdown.
   * Adds or removes the tag from the selection.
   * @param event - The autocomplete selection event.
   * @private
   */
  private _addExistingTags(event: MatAutocompleteSelectedEvent): void {
    let selectedValue = event.option.viewValue;
    let isSelectedValuePresent = this.control().value!.includes(selectedValue);

    if (isSelectedValuePresent) {
      // Remove if already selected
      this.remove(selectedValue);
    } else {
      // Add if not selected
      this.control().setValue([...this.control().value!, event.option.value]);
    }
    this.userTextInput.reset();
  }

  /**
   * Adds a new tag (not present in availableTags) to the selection and newTags.
   * If already selected, removes it instead.
   * @private
   */
  private _addNewTag(): void {
    const newTag = this.userTextInput.value?.trim();
    console.log('Adding new tag:', newTag);

    if (!newTag || newTag.length === 0) {
      return;
    }

    if (!this.control().value!.includes(newTag.toLowerCase())) {
      this.control().setValue([...this.control().value!, newTag]);
      this.newTags.update(tags => [...tags, newTag]);
    } else {
      this.remove(newTag);
    }
  }
}
