
import { AsyncPipe } from '@angular/common';
import { Component, model, output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { map, Observable, startWith } from 'rxjs';
import { localizePrize, unLocalizePrize } from '../../app.component';
import { Extension, extensionDisplayNames } from '../../models/extension';
import { inventoryItemDisplayNames } from '../../models/inventory-item';
import { AuthenticationService } from '../../services/authentication.service';
import { CacheInventoryService } from '../../services/cache-inventory.service';


@Component({
  selector: 'app-extension-editor',
  imports: [
    MatFormFieldModule,
    ReactiveFormsModule,
    MatInputModule,
    MatAutocompleteModule,
    AsyncPipe,
    MatDividerModule,
    MatButtonModule,
  ],
  templateUrl: './extension-editor.component.html',
  styleUrl: './extension-editor.component.css'
})
export class ExtensionEditorComponent {
  /**
   * Holds the current extension being edited.
   */
  extension = model<Extension>({} as Extension);

  /**
   * List of disabled input field keys.
   */
  disabledInputs = model<string[]>([]);
  /**
   * List of required input field keys.
   */
  requiredInputs = model<string[]>(['company', 'price', 'description']);

  /**
   * Event emitter that emits when the form validity changes.
   * Emits a boolean indicating the validity of the form.
   */
  isValid = output<boolean>();

  /**
   * Event emitter that emits the current extension object when it changes.
   * This is used to notify parent components of changes to the extension.
   */
  changes = output<Partial<Extension>>();

  /**
   * Stores the initial values of the extension for change detection.
   */
  initialValues!: Extension;

  /**
   * Defines the fields to display in the editor and their labels (for extensions).
   */
  extensionColumns = extensionDisplayNames;

  /**
   * Map of FormControl objects for each extension property.
   */
  formControls = new Map<string, FormControl>(
    Array.from(extensionDisplayNames.keys()).map(key => [key, new FormControl('')])
  );
  /**
   * FormGroup containing all form controls for validation and value tracking.
   */
  formGroup = new FormGroup(Object.fromEntries(this.formControls.entries()));


  /**
   * Map of autocomplete options for each field.
   */
  options = new Map<string, string[]>();
  /**
   * Map of filtered autocomplete options as observables for each field.
   */
  filteredOptions = new Map<string, Observable<string[]>>();

  constructor(
    private readonly cache: CacheInventoryService,
    private readonly authService: AuthenticationService) { }

  /**
   * Angular lifecycle hook.
   * Initializes form controls, sets up autocomplete, disables fields as specified,
   * and sets default values for new extensions.
   */
  ngOnInit() {
    if (!this.extension()) {
      this.extension.set({} as Extension);
    }
    if (this.disabledInputs() === undefined) {
      this.disabledInputs.set(['created_at']);
    }
    if (this.requiredInputs() === undefined) {
      this.requiredInputs.set(['company', 'price', 'description']);
    }

    this._setupFormControls();
    this._setupAutocomplete();

    this.isValid.emit(this.formGroup.valid && this.requiredInputs().every(input => this.formControls.get(input)?.value));
  }

  /**
   * Angular lifecycle hook.
   * Resets form controls and values when the inventory item changes.
   */
  ngOnChanges() {
    this.initialValues = { ...this.extension() };
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
    this.formGroup.valueChanges.subscribe(value => {
      this.extension.update(extension => {
        for (const [key, control] of this.formControls.entries()) {
          (extension as any)[key] = key === 'price' ? unLocalizePrize(control.value) : control.value;
        }
        return extension;
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
    this.formControls.get('price')?.setValue(
      this.formControls.get('price')?.value ? localizePrize(this.formControls.get('price')?.value) : this.formControls.get('price')?.value, { emitEvent: false });
  }

  private _getChanges(): Partial<Extension> {
    const changes: Partial<Extension> = {};
    for (const [key, control] of this.formControls.entries()) {
      if (control.dirty && control.value !== this.initialValues[key as keyof Extension]) {
        changes[key as keyof Extension] = key === 'price' ? unLocalizePrize(control.value) : control.value;
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
    for (const key of inventoryItemDisplayNames.keys()) {
      this.options.set(key, [] as string[]);
    }
    this.cache.getCompanies().subscribe(companies => this.options.set('company', companies));
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
