import {LiveAnnouncer} from '@angular/cdk/a11y';
import {COMMA, ENTER} from '@angular/cdk/keycodes';
import {
  Component,
  computed, effect,
  EventEmitter, input,
  Input,
  model,
  Output,
  signal
} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {
  MatAutocomplete,
  MatAutocompleteModule,
  MatAutocompleteSelectedEvent,
  MatAutocompleteTrigger, MatOption
} from '@angular/material/autocomplete';
import {MatChipGrid, MatChipInput, MatChipInputEvent, MatChipRow, MatChipsModule} from '@angular/material/chips';
import {MatFormField, MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';

@Component({
  selector: 'app-chip',
  imports: [
    FormsModule,
    MatChipInput,
    MatAutocompleteTrigger,
    MatChipRow,
    MatChipGrid,
    MatFormField,
    MatAutocomplete,
    MatOption,
    MatIconModule,
    MatFormFieldModule,
    MatChipsModule,
    MatAutocompleteModule,
  ],
  templateUrl: './chip.component.html',
  styleUrl: './chip.component.css'
})
export class ChipComponent {
  readonly separatorKeysCodes: number[] = [ENTER, COMMA];
  readonly currentFruit = model('');
  readonly selectedOptions = signal<string[]>([]);

  readonly filteredSelectedOptions = computed(() => {
    const currentFruit = this.currentFruit().toLowerCase();
    return currentFruit
      ? this.options().filter(fruit => fruit.toLowerCase().includes(currentFruit))
      : this.options().slice();
  });

  inputPlaceholder = input<string>("Put some placeholder here...");
  options = input<string[]>(["Example 1", "Example 2", "Example 3"]);

  @Output()
  changes: EventEmitter<string[]> = new EventEmitter();

  constructor(private announcer: LiveAnnouncer) {
    effect(() => {
      this.changes.emit(this.selectedOptions())
    });
  }

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    // Add our fruit
    if (value) {
      this.selectedOptions.update(selectedOptions => [...selectedOptions, value]);
    }

    // Clear the input value
    this.currentFruit.set('');
  }

  remove(fruit: string): void {
    this.selectedOptions.update(selectedOptions => {
      const index = selectedOptions.indexOf(fruit);
      if (index < 0) {
        return selectedOptions;
      }

      selectedOptions.splice(index, 1);
      this.announcer.announce(`Removed ${fruit}`);
      return [...selectedOptions];
    });
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    this.selectedOptions.update(selectedOptions => [...selectedOptions, event.option.viewValue]);
    this.currentFruit.set('');
    event.option.deselect();
  }
}
