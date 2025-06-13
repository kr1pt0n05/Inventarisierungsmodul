import { AsyncPipe } from '@angular/common';
import { Component, model } from '@angular/core';
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
 *
 * Properties:
 * - inventoryItem: Model holding the current inventory item being edited.
 * - inventoryItemColumns: Map defining the fields to display and their labels.
 * - formControls: Map of FormControl objects for each inventory item property.
 * - formGroup: FormGroup containing all form controls for validation and value tracking.
 *
 * Methods:
 * - ngOnInit(): Initializes form controls with the current inventory item values and sets up synchronization between the form and the model.
 *
 * Usage:
 * Place <app-inventory-item-editor> in your template. Bind to its inputs and outputs as needed.
 *
 * Dependencies:
 * - Angular Reactive Forms and Material modules.
 * - InventoryItem model.
 */
@Component({
  selector: 'app-inventory-item-editor',
  imports: [
    CardComponent,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatFormFieldModule,
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
  disabledInputs = model<Map<string, boolean>>(new Map<string, boolean>());

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
   * Initializes the form controls with the current inventory item values.
   * Sets up a subscription to synchronize form changes with the inventory item model.
   */
  ngOnInit() {
    this._setupFormControls();
    this._setupAutocomplete();

    for (const [key, control] of this.formControls.entries()) {
      if (this.disabledInputs().get(key)) {
        control.disable();
        console.log(`Input ${key} is disabled.`);
      }
    }
  }

  private _setupFormControls() {
    if (this.inventoryItem()) {
      for (const [key, control] of this.formControls.entries()) {
        control.setValue(this.inventoryItem()![key as keyof InventoryItem]) ?? '';
      }
    }

    this.formGroup.valueChanges.subscribe(value => {
      this.inventoryItem.update(item => {
        for (const [key, control] of this.formControls.entries()) {
          item![key as keyof InventoryItem] = control.value;
        }
        return item;
      });
    });

    if (!this.inventoryItem().created_at) {
      this.formControls.get('created_at')?.setValue(new Date().toISOString().split('T')[0]);
    }
    if (!this.inventoryItem().orderer) {
      this.formControls.get('orderer')?.setValue(this.authService.getUsername() ?? '-');
    }

  }

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

  private _filter(value: string, id: string): string[] {
    const filterValue = value.toLowerCase();
    return this.options.get(id)?.filter(option => option.toLowerCase().includes(filterValue)) ?? [];
  }
  // TODO: Implement auto complete, validation

}
