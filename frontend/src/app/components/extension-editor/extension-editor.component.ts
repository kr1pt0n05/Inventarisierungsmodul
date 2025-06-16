import { AsyncPipe } from '@angular/common';
import { Component, EventEmitter, model, Output, signal, WritableSignal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ActivatedRoute, Router } from '@angular/router';
import { map, Observable, startWith } from 'rxjs';
import { Extension, extensionDisplayNames, extensionItemFromArticle } from '../../models/extension';
import { InventoryItem, inventoryItemDisplayNames } from '../../models/inventory-item';
import { ArticleId } from '../../models/Order';
import { AuthenticationService } from '../../services/authentication.service';
import { CacheInventoryService } from '../../services/cache-inventory.service';
import { InventoriesService } from '../../services/inventories.service';
import { OrderService } from '../../services/order.service';
import { CardComponent } from '../card/card.component';
import { InventoryListComponent } from "../inventory-list/inventory-list.component";

@Component({
  selector: 'app-extension-editor',
  imports: [
    CardComponent,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatInputModule,
    MatAutocompleteModule,
    AsyncPipe,
    InventoryListComponent,
    MatDividerModule,
    MatButtonModule,
    MatInputModule,

  ],
  templateUrl: './extension-editor.component.html',
  styleUrl: './extension-editor.component.css'
})
export class ExtensionEditorComponent {

  constructor(
    private readonly cache: CacheInventoryService,
    private readonly authService: AuthenticationService,
    private readonly inventoriesService: InventoriesService,
    private readonly orderService: OrderService,
    private readonly router: Router, route: ActivatedRoute) {
    route.queryParams.subscribe(val => {
      // put the code from ngOnInit here 
      console.log('Query params:', val);


      if (val['extensionArticles']) {
        this.extensionArticles.set([...val['extensionArticles']]);
        console.log('Extension articles set from query params:', this.extensionArticles());
      } else {
        this.extensionArticles.set([]);
      }

      if (val['inventoryId']) {
        this.inventoryId.set(Number(val['inventoryId']));
      } else {
        this.inventoryId.set(undefined);
      }

      if (this.extensionArticles().length > 0) {
        //this._setArticleAsInventoryItem(this.extensionArticles);
        this._setArticleAsExtension(this.extensionArticles);
        console.log('Set extension from article', this.extension());
      }

    });
  }
  /**
   * Holds the current inventory item being edited.
   */
  extension = model<Extension>({} as Extension);
  inventoryId = model<number | undefined>(undefined);
  inventoryItem = {} as InventoryItem;
  disabledInputs = model<string[]>([]);
  requiredInputs = model<string[]>(['cost_center', 'company', 'price', 'description']);
  initialValues!: Extension;
  extensionArticles = signal<string[]>([]);

  currentArticleId: ArticleId = {} as ArticleId;

  isNewExtension = model<boolean>(false);

  /**
   * Event emitter that emits when the form is valid or invalid.
   * Emits a boolean indicating the validity of the form.
   */
  @Output() isValid = new EventEmitter<boolean>(false);

  /**
   * Defines the fields to display in the editor and their labels.
   */
  extensionColumns = extensionDisplayNames;
  inventoryItemColumns = inventoryItemDisplayNames;

  /**
   * Map of FormControl objects for each inventory item property.
   */
  formControls = new Map<string, FormControl>(
    Array.from(this.extensionColumns.keys()).map(key => [key, new FormControl('')])
  );

  inventoryIdControl = new FormControl('');

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
    if (this.isNewExtension() === undefined) {
      this.isNewExtension.set(false);
    }
    if (this.extension()) {
      console.log('Extension initialized:', this.extension());
    } else {
      console.warn('Extension is not initialized. Please provide a valid extension object.');
      this.extension.set({} as Extension);
    }
    if (this.disabledInputs() === undefined) {
      this.disabledInputs.set(this.isNewExtension() ? ['created_at'] : []);
    }
    if (this.requiredInputs() === undefined) {
      this.requiredInputs.set(['cost_center', 'company', 'price', 'description']);
    }

    this._setupFormControls();
    this._setupAutocomplete();

    this._updateValuesFromInput();


  }

  ngOnChanges() {
    // Reset form controls when inventoryItem changes
    this._onChanges();
  }

  onSelectInventory() {
    this.inventoryId.set(Number(this.inventoryIdControl.value));
    this._onChanges();
  }

  private _onChanges() {

    if (this.inventoryId() !== undefined) {
      this.inventoriesService.getInventoryById(this.inventoryId()!).subscribe({
        next: (inventory) => {
          this.inventoryItem = inventory;
        },
        error: (error) => {
          this.inventoryItem = {} as InventoryItem;
          this.inventoryId.set(undefined);
          console.error('Error fetching inventory item:', error);
        }
      });
    }

    this.initialValues = { ...this.extension() };
    this._updateValuesFromInput();
    this.formGroup.markAsPristine();
    this.formGroup.markAsUntouched();

    if (this.isNewExtension()) {
      this.disabledInputs.set(['created_at']);
    }
    console.log('Disabled inputs:', this.disabledInputs(), this.isNewExtension());

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
    this.formGroup.valueChanges.subscribe(value => {
      this.extension.update(extension => {
        for (const [key, control] of this.formControls.entries()) {
          (extension! as any)[key] = control.value;
        }
        return extension;
      });

      this.isValid.emit(this.formGroup.valid && this.requiredInputs().every(input => this.formControls.get(input)?.value));
    });
  }

  private _updateValuesFromInput() {
    if (this.initialValues) {
      for (const [key, control] of this.formControls.entries()) {
        control.setValue(this.initialValues![key as keyof Extension] ?? '');
      }
    }
    console.log(this.extension());
    if (!this.extension().created_at) {
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
    if (!this.extension().orderer) {
      this.formControls.get('orderer')?.setValue(this.authService.getUsername());
    }
  }

  private _setArticleAsExtension(articleStrings: WritableSignal<string[]>) {
    [this.currentArticleId.orderId, this.currentArticleId.articleId] = articleStrings()[0].split(',').map(Number);
    this.orderService.getOrderArticleByIds(this.currentArticleId.orderId, this.currentArticleId.articleId).subscribe({
      next: (article) => {
        console.log('Fetched article:', article);
        this.extension.set(extensionItemFromArticle(article!));
        articleStrings.update(articles => articles.slice(1)); // Remove the first article after setting it
        this._onChanges();
      },
      error: (error) => {
        console.error('Error fetching order article:', error);
      }
    });
  }

  onInventorization() {
    console.log('Inventorization completed:', this.extension());

    if (this.extensionArticles().length > 0) {
      this.router.navigate(['/new-extension'], { queryParams: { inventoryId: this.inventoryId(), extensionArticles: [...this.extensionArticles()] } });
    } else if (this.inventoryId() !== undefined) {
      this.router.navigate(['/inventory/', this.inventoryId()]);
    } else {
      this.router.navigate(['/orders'])
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
    const filterValue = String(value).toLowerCase() ?? '';
    return this.options.get(id)?.filter(option => option.toLowerCase().includes(filterValue)) ?? [];
  }

}
