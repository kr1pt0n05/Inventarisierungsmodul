import {LiveAnnouncer} from '@angular/cdk/a11y';
import {COMMA, ENTER} from '@angular/cdk/keycodes';
import {
  ChangeDetectionStrategy,
  Component,
  computed, effect,
  EventEmitter,
  inject,
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
import {MatIcon, MatIconModule} from '@angular/material/icon';
import {MatLabel} from '@angular/material/input';
import {MatSelect} from '@angular/material/select';

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
  readonly fruits = signal(['8500005110']);
  @Input()
  allFruits: string[] = ['850302', '850000', '715501', '850206', '850302', '850000', '715501', '850206', '850302', '850000', '715501', '850206', '850302', '850000', '715501', '850206'];

  readonly filteredFruits = computed(() => {
    const currentFruit = this.currentFruit().toLowerCase();
    return currentFruit
      ? this.allFruits.filter(fruit => fruit.toLowerCase().includes(currentFruit))
      : this.allFruits.slice();
  });

  @Input()
  inputPlaceholder: string = "Eintrag hinzufügen";
  @Output()
  changes: EventEmitter<string[]> = new EventEmitter();

  constructor(private announcer: LiveAnnouncer) {
    effect(() => {
      this.changes.emit(this.fruits())
    });
  }

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    // Add our fruit
    if (value) {
      this.fruits.update(fruits => [...fruits, value]);
    }

    // Clear the input value
    this.currentFruit.set('');
  }

  remove(fruit: string): void {
    this.fruits.update(fruits => {
      const index = fruits.indexOf(fruit);
      if (index < 0) {
        return fruits;
      }

      fruits.splice(index, 1);
      this.announcer.announce(`Removed ${fruit}`);
      return [...fruits];
    });
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    this.fruits.update(fruits => [...fruits, event.option.viewValue]);
    this.currentFruit.set('');
    event.option.deselect();
  }
}
