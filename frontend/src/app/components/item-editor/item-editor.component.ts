import { AsyncPipe } from '@angular/common';
import { Component, input, model, output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { map, Observable, startWith } from 'rxjs';
import { environment } from '../../../environment';
import { localizePrice, unLocalizePrice } from '../../app.component';
import { Extension } from '../../models/extension';
import { InventoryItem } from '../../models/inventory-item';
import { AuthenticationService } from '../../services/authentication.service';
import { CacheInventoryService } from '../../services/cache-inventory.service';
import { InventoriesService } from '../../services/inventories.service';

@Component({
  selector: 'app-item-editor',
  imports: [
    MatFormFieldModule,
    ReactiveFormsModule,
    MatInputModule,
    MatAutocompleteModule,
    AsyncPipe,
    MatDividerModule,
    MatButtonModule,
  ],
  templateUrl: './item-editor.component.html',
  styleUrl: './item-editor.component.css'
})
export class ItemEditorComponent {
  /**
   * Holds the current extension being edited.
   */
  item = model<Extension | InventoryItem>({} as InventoryItem);

  /**
   * List of disabled input field keys.
   */
  disabledInputs = model<string[]>(['created_at']);
  /**
   * List of required input field keys.
   */
  requiredInputs = model<string[]>([]);

  /**
   * Event emitter that emits when the form validity changes.
   * Emits a boolean indicating the validity of the form.
   */
  isValid = output<boolean>();

  /**
   * Event emitter that emits the current extension object when it changes.
   * This is used to notify parent components of changes to the extension.
   */
  changes = output<Partial<Extension | InventoryItem>>();

  /**
   * Stores the initial values of the extension for change detection.
   */
  initialValues!: Extension | InventoryItem;

  /**
   * Defines the fields to display in the editor and their labels (for extensions).
   */
  itemColumns = input.required<Map<string, string>>();

  /**
   * Map of FormControl objects for each extension property.
   */
  formControls!: Map<string, FormControl>;

  /**
   * FormGroup containing all form controls for validation and value tracking.
   */
  formGroup!: FormGroup;


  /**
   * Map of autocomplete options for each field.
   */
  options = new Map<string, string[]>();
  /**
   * Map of filtered autocomplete options as observables for each field.
   */
  filteredOptions = new Map<string, Observable<string[]>>();

  isInitialized = false;

  constructor(
    private readonly cache: CacheInventoryService,
    private readonly authService: AuthenticationService,
    private readonly inventoriesService: InventoriesService) { }

  /**
   * Angular lifecycle hook.
   * Initializes form controls, sets up autocomplete, disables fields as specified,
   * and sets default values for new extensions.
   */
  ngOnInit() {
    this._setupFormControls();
    this._setupAutocomplete();

    this.isValid.emit(this.formGroup.valid && this.requiredInputs().every(input => this.formControls.get(input)?.value));
    this.isInitialized = true;
    this.ngOnChanges();
  }

  /**
   * Angular lifecycle hook.
   * Resets form controls and values when the inventory item changes.
   */
  ngOnChanges() {
    if (!this.isInitialized) {
      return;
    }
    this.initialValues = { ...this.item() };
    this._updateValuesFromInput();
    this.formGroup.markAsPristine();
    this.formGroup.markAsUntouched();

    for (const [key, control] of this.formControls.entries()) {
      if (this.disabledInputs().includes(key)) {
        control.disable();
      }
    }
  }

  /**
   * Sets up form controls with initial values from the extension,
   * synchronizes form changes with the model, and emits form validity.
   * @private
   */
  private _setupFormControls() {
    this.formControls = new Map<string, FormControl>(
      Array.from(this.itemColumns().keys()).map(key => [key as string, new FormControl('')])
    );
    this.formGroup = new FormGroup(Object.fromEntries(this.formControls.entries()));

    this.formControls.get('price')?.addValidators([
      Validators.pattern(environment.priceRegEx)
    ]);

    this.formGroup.valueChanges.subscribe(value => {
      this.item.update(item => {
        for (const [key, control] of this.formControls.entries()) {
          item[key] = key === 'price' ? unLocalizePrice(control.value) : control.value;
        }
        return item;
      });

      this.isValid.emit(this.formGroup.valid && this.requiredInputs().every(input => this.formControls.get(input)?.value));

      this.changes.emit(this._getChanges());
    });
  }

  /**
   * Updates form controls with values from the initial extension.
   * Sets default values for 'created_at' and 'orderer' if not already set.
   * @private
   */
  private _updateValuesFromInput() {
    if (this.initialValues) {
      for (const [key, control] of this.formControls.entries()) {
        control.setValue(this.initialValues[key as keyof Extension] ?? '');
      }
    }
    if (!this.initialValues.created_at) {
      this.formControls.get('created_at')?.setValue(new Date().toLocaleString("de-De",
        {
          day: '2-digit',
          month: '2-digit',
          year: 'numeric',
          hour: '2-digit',
          minute: '2-digit',
          second: '2-digit'
        }).replace(',', ''));
    }
    if (!this.initialValues.orderer) {
      this.formControls.get('orderer')?.setValue(this.authService.getUsername());
    }
    if (this.formControls.has('id') && !this.initialValues.id) {
      this.inventoriesService.getMinAndMaxId().subscribe({
        next: (minAndMaxId) => {
          this.formControls.get('id')!.setValue(minAndMaxId.maxId + 1)
        },
        error: (error) => {
          console.error('Error fetching min and max ID:', error);
        }
      });
    }
    this.formControls.get('price')?.setValue(
      localizePrice(this.formControls.get('price')?.value) ?? '', { emitEvent: false });
  }

  private _getChanges(): Partial<Extension | InventoryItem> {
    const changes: Partial<Extension | InventoryItem> = {};
    for (const [key, control] of this.formControls.entries()) {
      const value = key === 'price' ? unLocalizePrice(control.value) : control.value;
      if (control.dirty && value !== this.initialValues[key]) {
        changes[key] = value;
      }
    }
    return changes;
  }

  /**
   * Initializes autocomplete options for relevant fields using cached values,
   * and sets up filtered observables for each field.
   * @private
   */
  private _setupAutocomplete() {
    for (const key of this.itemColumns().keys()) {
      this.options.set(key, [] as string[]);
    }
    this.cache.getCostCenters().subscribe(costCenters => this.options.set('cost_center', costCenters));
    this.cache.getCompanies().subscribe(companies => this.options.set('company', companies));
    this.cache.getLocations().subscribe(locations => this.options.set('location', locations));
    this.cache.getOrderers().subscribe(orderers => this.options.set('orderer', orderers));

    for (const [key, control] of this.formControls.entries()) {
      this.filteredOptions.set(key, control.valueChanges
        .pipe(
          startWith(''),
          map(value => this._filter(value ?? '', key))
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
    const filterValue = String(value).toLowerCase() ?? '';
    return this.options.get(id)?.filter(option => option.toLowerCase().includes(filterValue)) ?? [];
  }

}
