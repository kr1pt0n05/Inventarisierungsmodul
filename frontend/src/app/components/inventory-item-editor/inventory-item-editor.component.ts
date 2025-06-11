import { Component, model } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { InventoryItem } from '../../models/inventory-item';
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
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
  ],
  templateUrl: './inventory-item-editor.component.html',
  styleUrl: './inventory-item-editor.component.css'
})
export class InventoryItemEditorComponent {
  /**
   * Holds the current inventory item being edited.
   */
  inventoryItem = model<InventoryItem>({} as InventoryItem);

  /**
   * Defines the fields to display in the editor and their labels.
   */
  inventoryItemColumns = new Map<string, string>([
    ['costCenter', 'Kostenstelle'],
    ['id', 'Inventarnummer'],
    ['description', 'Geräte-/Softwaretyp'],
    ['company', 'Firma'],
    ['price', 'Preis'],
    ['createdAt', 'Bestelldatum'],
    ['serialNumber', 'Seriennummer'],
    ['location', 'Standort/Nutzer:in'],
    ['orderer', 'Bestellt von']
  ]);

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

  /**
   * Initializes the form controls with the current inventory item values.
   * Sets up a subscription to synchronize form changes with the inventory item model.
   */
  ngOnInit() {
    if (this.inventoryItem()) {
      for (const [key, control] of this.formControls.entries()) {
        control.setValue(this.inventoryItem()![key as keyof InventoryItem]);
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
  }

  // TODO: Implement auto complete, validation

}
