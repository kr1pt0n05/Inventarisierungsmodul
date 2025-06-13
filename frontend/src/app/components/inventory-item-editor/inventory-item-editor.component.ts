import { AsyncPipe } from '@angular/common';
import { Component, EventEmitter, model, Output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { map, Observable, startWith } from 'rxjs';
import { InventoryItem, inventoryItemDisplayNames } from '../../models/inventory-item';
import { AuthenticationService } from '../../services/authentication.service';
import { CacheInventoryService } from '../../services/cache-inventory.service';
import { CardComponent } from "../card/card.component";

/**
 * InventoryItemEditorComponent
 *
 * This Angular component provides a form-based editor for inventory items.
 * It allows users to view and edit the properties of an InventoryItem model.
 *
 * Features:
 * - Dynamically generates form controls for each inventory item property.
 * - Synchronizes form values with the InventoryItem model.
 * - Supports two-way data binding between the form and the model.
 * - Provides autocomplete functionality for selected fields using cached values.
 * - Automatically fills certain fields (created_at, orderer) with default values if not set.
 * - Allows disabling of specific input fields via the disabledInputs model.
 *
 * Properties:
 * - inventoryItem: Model holding the current inventory item being edited.
 * - disabledInputs: Model holding a map of input fields that should be disabled.
 * - inventoryItemColumns: Map defining the fields to display and their labels.
 * - formControls: Map of FormControl objects for each inventory item property.
 * - formGroup: FormGroup containing all form controls for validation and value tracking.
 * - options: Map of autocomplete options for each field.
 * - filteredOptions: Map of filtered autocomplete options as observables for each field.
 *
 * Methods:
 * - ngOnInit(): Initializes form controls with the current inventory item values, sets up synchronization, disables fields as needed, and initializes autocomplete.
 * - _setupFormControls(): Sets up form controls with initial values, synchronizes form changes with the model, and fills default values for certain fields.
 * - _setupAutocomplete(): Initializes autocomplete options for relevant fields and sets up filtered observables for each.
 * - _filter(value, id): Filters the autocomplete options for a given field based on the input value.
 *
 * Usage:
 * Place <app-inventory-item-editor> in your template. Bind to its inputs and outputs as needed.
 *
 * Dependencies:
 * - Angular Reactive Forms and Material modules.
 * - InventoryItem model.
 * - AuthenticationService for user information.
 * - CacheInventoryService for cached autocomplete values.
 */
@Component({
  selector: 'app-inventory-item-editor',
  imports: [
    CardComponent,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatInputModule,
    MatAutocompleteModule,
    AsyncPipe
  ],
  templateUrl: './inventory-item-editor.component.html',
  styleUrl: './inventory-item-editor.component.css'
})
export class InventoryItemEditorComponent {
  constructor(private readonly cache: CacheInventoryService, private readonly authService: AuthenticationService) { }
  /**
   * Holds the current inventory item being edited.
   */
  inventoryItem = model<InventoryItem>({} as InventoryItem);
  disabledInputs = model<string[]>([]);
  requiredInputs = model<string[]>(['id', 'cost_center', 'company', 'orderer']);
  initialValues!: InventoryItem;

  /**
   * Event emitter that emits when the form is valid or invalid.
   * Emits a boolean indicating the validity of the form.
   */
  @Output() isValid = new EventEmitter<boolean>(false);

  /**
   * Defines the fields to display in the editor and their labels.
   */
  inventoryItemColumns = inventoryItemDisplayNames;

  /**
   * Map of FormControl objects for each inventory item property.
   */
  formControls = new Map<string, FormControl>(
    Array.from(this.inventoryItemColumns.keys()).map(key => [key, new FormControl('')])
  );

  /**
   * FormGroup containing all form controls for validation and value tracking.
   */
  formGroup = new FormGroup(Object.fromEntries(this.formControls.entries()));

  options = new Map<string, string[]>();
  filteredOptions = new Map<string, Observable<string[]>>();

  /**
   * Angular lifecycle hook.
   * Initializes form controls, sets up autocomplete, and disables fields as specified.
   */
  ngOnInit() {
    this.initialValues = { ...this.inventoryItem() };

    this._setupFormControls();
    this._setupAutocomplete();

    for (const [key, control] of this.formControls.entries()) {
      if (this.disabledInputs().includes(key)) {
        control.disable();
        console.log(`Input ${key} is disabled.`);
      }
    }
  }

  /**
   * Sets up form controls with initial values from the inventory item,
   * synchronizes form changes with the model, checks if the data is valid and
   * fills default values for 'created_at' and 'orderer' if not already set.
   * @private
   */
  private _setupFormControls() {
    if (this.inventoryItem()) {
      for (const [key, control] of this.formControls.entries()) {
        control.setValue(this.inventoryItem()![key as keyof InventoryItem] ?? '');
      }
    }

    this.formGroup.valueChanges.subscribe(value => {
      this.inventoryItem.update(item => {
        for (const [key, control] of this.formControls.entries()) {
          item![key as keyof InventoryItem] = control.value;
        }
        return item;
      });

      this.isValid.emit(this.formGroup.valid && this.requiredInputs().every(input => this.formControls.get(input)?.value));
    });

    if (!this.inventoryItem().created_at) {
      this.formControls.get('created_at')?.setValue(new Date().toISOString().split('T')[0]);
    }
    if (!this.inventoryItem().orderer) {
      this.formControls.get('orderer')?.setValue(this.authService.getUsername() ?? '-');
    }

  }

  /**
   * Initializes autocomplete options for relevant fields using cached values,
   * and sets up filtered observables for each field.
   * @private
   */
  private _setupAutocomplete() {
    for (const key of inventoryItemDisplayNames.keys()) {
      this.options.set(key, [] as string[]);
    }
    this.cache.getCostCenters().subscribe(costCenters => this.options.set('cost_center', costCenters));
    this.cache.getCompanies().subscribe(companies => this.options.set('company', companies));
    this.cache.getSerialNumbers().subscribe(serialNumbers => this.options.set('serial_number', serialNumbers));
    this.cache.getLocations().subscribe(locations => this.options.set('location', locations));
    this.cache.getOrderers().subscribe(orderers => this.options.set('orderer', orderers));

    for (const [key, control] of this.formControls.entries()) {
      this.filteredOptions.set(key, control.valueChanges
        .pipe(
          startWith(''),
          map(value => this._filter(value || '', key))
        )
      );
    }

  }

  /**
   * Filters the autocomplete options for a given field based on the input value.
   * @param value The current input value.
   * @param id The field identifier.
   * @returns Filtered array of options for the field.
   * @private
   */
  private _filter(value: string, id: string): string[] {
    const filterValue = value.toLowerCase();
    return this.options.get(id)?.filter(option => option.toLowerCase().includes(filterValue)) ?? [];
  }
  // TODO: Implement validation and required fields

}
