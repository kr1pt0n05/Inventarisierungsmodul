import { Component, computed, input, signal } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import {
  MatAutocomplete,
  MatAutocompleteSelectedEvent,
  MatAutocompleteTrigger,
  MatOption
} from '@angular/material/autocomplete';
import {
  MatChipGrid, MatChipInput,
  MatChipRemove,
  MatChipRow,
} from '@angular/material/chips';
import { MatFormField } from '@angular/material/form-field';
import { MatIcon } from '@angular/material/icon';
import { MatLabel } from '@angular/material/input';
import { getTagColor } from '../../models/tag';


/**
 * ChipV2Component
 *
 * This component provides a chip-based input UI for selecting elements from a list. The selected items are displayed
 * as chips that can be removed, and the user can filter available options using a text input with an autocomplete feature.
 *
 * Features:
 * - Display a list of chips representing selected options.
 * - Autocomplete input for searching and selecting options.
 * - User can remove selected options by clicking an 'X' on the chip.
 * - Customizable options and placeholder for dynamic input and output.
 *
 * Dependencies:
 * - Angular Material components for chips, forms, and autocomplete.
 */
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

  /**
   * Custom labels for the chip input.
   * These are customizable from the parent component.
   */
  readonly heading = input("Elements to chose");
  readonly placeholder = input("Placeholder to chose");


  /**
   * List of available options for the user to choose from.
   * The options are passed from the parent component.
   * This list is used for filtering and displaying the options in the dropdown.
   */
  readonly options = input(["Element 5", "Element 6", "Lorem Ipsum", "Random"]);


  /**
   * FormControl that tracks the selected elements (chips).
   * The `control` holds the list of selected values, which are displayed as chips.
   * It is a required input and manages the selected items in the component.
   */
  control = input.required<FormControl<string[]>>(); // Tracks elements currently selected

  /**
   * Signal that indicates whether to use tag colors for the chips.
   * If true, the chips will be styled with colors based on the chips value.
   */
  useTagColors = input(false);


  /**
   * Tracks the user’s input in the autocomplete text field.
   * The value is used to filter the options list as the user types.
   */
  userTextInput = signal("");

  /**
   * Computed property that filters the available options based on the user's text input.
   * This filters the `options` array and returns only those items that include the text typed by the user.
   */
  selectedOptions = computed(() => {
    return this.options().filter(option =>
      option.toLowerCase().includes(this.userTextInput().toLowerCase())
    );
  });


  /**
   * Removes a selected option from the `control` (the list of selected chips).
   * This is triggered when the user clicks the 'X' button on a chip.
   *
   * @param element - The option to remove from the selected chips.
   */
  remove(element: string): void {
    this.control().setValue(this.control().value.filter(v => v !== element));
  }

  /**
   * Handles the selection of an option from the autocomplete dropdown.
   * If the option is already selected, it is removed from the selected list.
   * If it is not selected, it is added to the list of selected options (chips).
   * The user's input is cleared after selecting an option, resetting the autocomplete.
   *
   * @param event - The event triggered when the user selects an option from the dropdown.
   */
  selected(event: MatAutocompleteSelectedEvent): void {
    let selectedValue = event.option.viewValue; // Get the value of the selected option
    let isSelectedValuePresent = this.control().value.includes(selectedValue); // Check if the option is already selected

    // If the value is already selected, remove it from the list
    if (isSelectedValuePresent) {
      // remove element from list
      this.remove(selectedValue);

    } else {
      // If the value is not selected, add it to the list of selected values
      this.control().setValue([...this.control().value, event.option.value]);
    }

    // Reset the autocomplete input to show the full list again
    this.userTextInput.set("");
  }

  getTagColor(element: string): string {
    if (this.useTagColors()) {
      return getTagColor(element);
    }
    return '';
  }

}
