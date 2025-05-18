import {Component, computed, input, signal} from '@angular/core';
import {
  MatChipGrid, MatChipInput,
  MatChipRemove,
  MatChipRow,
} from '@angular/material/chips';
import {MatFormField} from '@angular/material/form-field';
import {MatLabel} from '@angular/material/input';
import {MatIcon} from '@angular/material/icon';
import {FormControl, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {
  MatAutocomplete,
  MatAutocompleteSelectedEvent,
  MatAutocompleteTrigger,
  MatOption
} from '@angular/material/autocomplete';

@Component({
  selector: 'app-chip-v2',
  imports: [
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
  ],
  templateUrl: './chip-v2.component.html',
  styleUrl: './chip-v2.component.css'
})
export class ChipV2Component {

  // Custom labels
  readonly heading = input("Elements to chose");
  readonly placeholder = input("Placeholder to chose");

  // Custom values & currently selected value
  readonly options = input(["Element 5", "Element 6", "Lorem Ipsum", "Random"]);  // Elements to chose from drop down list
  control = input.required<FormControl<string[]>>(); // Tracks elements currently selected

  // Tracks input that user is currently typing to autocomplete, only needed for filtering options to be displayed on drop down menu
  userTextInput = signal("");
  selectedOptions = computed(() => {
    return this.options().filter(option =>
      option.toLowerCase().includes(this.userTextInput().toLowerCase())
    );
  });


  // Remove element from list
  // Triggered when 'X' button of selected element is clicked
  remove(element: string): void {
    this.control().setValue(this.control().value!.filter(v => v !== element));
  }

  // Triggered, when user clicks an element of the drop down list.
  // Adds clicked element to list, if not present.
  // Removes element from list, if already present.
  // Clears currently typed input to autocomplete, so full list will be displayed
  selected(event: MatAutocompleteSelectedEvent): void {
    let selectedValue = event.option.viewValue;
    let isSelectedValuePresent = this.control().value.includes(selectedValue);

    if(isSelectedValuePresent) {
      // remove element from list
      this.remove(selectedValue);

    }else{
      // add to list
      this.control().setValue([...this.control().value!, event.option.value]);
    }

    // Reset autocomplete to display full drop down list
    this.userTextInput.set("");
  }

}
