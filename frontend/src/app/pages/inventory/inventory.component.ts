import {
  Component, inject, OnInit,
  QueryList,
  signal,
  ViewChildren,
} from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { AccordionComponent } from '../../components/accordion/accordion.component';
import { CardComponent } from '../../components/card/card.component';
import { ChipV2Component } from '../../components/chip-v2/chip-v2.component';
import { DatepickerComponent } from '../../components/datepicker/datepicker.component';
import { InventoryListComponent } from '../../components/inventory-list/inventory-list.component';
import { RangeSliderComponent } from '../../components/range-slider/range-slider.component';
import { CacheInventoryService } from '../../services/cache-inventory.service';
import { ServerTableDataSourceService } from '../../services/server-table-data-source.service';



export interface minAndMaxId {
  maxId: number,
  minId: number,
}

export interface minAndMaxPrice {
  maxPrice: number,
  minPrice: number,
}

/**
 * InventoryComponent
 *
 * This Angular component is responsible for displaying an inventory filtering interface.
 * It allows users to filter and view inventory items based on various criteria, including cost centers,
 * companies, price range, serial numbers, and more.
 *
 * Features:
 * - Provides a form-based UI for filtering inventory items.
 * - Supports dynamic data loading for filter options (cost centers, companies, serial numbers, etc.).
 * - Integrates with custom components such as `RangeSliderComponent`, `DatepickerComponent`, and `AccordionComponent`.
 * - Uses a cache service to reduce API calls and improve performance.
 *
 * Properties:
 * - `serverTableDataSource`: The data source service that handles filtering and fetching of inventory items from the API.
 * - `cache`: The service that handles the retrieval of cached data for filter options.
 * - `accordions`: A reference to the `AccordionComponent` instances, used for controlling the visibility of accordion sections.
 * - `inventoryForm`: The form group containing form controls used to filter inventory items.
 * - `costCenters`, `companies`, `serialNumbers`, `locations`, `orderers`, `tags`: Arrays holding filter options that are fetched from the cache service.
 *
 * Methods:
 * - `ngOnInit()`: Initializes the form and fetches the filter options from the cache service.
 * - `openAllAccordion()`: Opens all sections of the accordion.
 * - `closeAllAccordions()`: Closes all sections of the accordion.
 *
 * Usage:
 * Place `<app-inventory>` in your template. The component will automatically manage inventory filtering and display.
 *
 * Dependencies:
 * - Angular Material components for UI elements such as buttons.
 * - ReactiveFormsModule for managing form controls.
 * - Custom components (`CardComponent`, `RangeSliderComponent`, etc.) for UI and interaction.
 * - `ServerTableDataSourceService` and `CacheInventoryService` for data handling and caching.
 */
@Component({
  selector: 'app-inventory',
  imports: [
    CardComponent,
    RangeSliderComponent,
    DatepickerComponent,
    InventoryListComponent,
    AccordionComponent,
    MatButtonModule,
    ChipV2Component,
    MatCheckboxModule,
    MatIconModule
  ],
  templateUrl: './inventory.component.html',
  styleUrl: './inventory.component.css'
})
export class InventoryComponent implements OnInit {

  /**
   * The data source service that handles the filtering and fetching of inventory items from the API.
   * This service is injected into the component for dynamic handling of inventory data.
   */
  serverTableDataSource = inject(ServerTableDataSourceService);

  /**
   * The cache service that retrieves cached inventory filter options such as cost centers, companies, etc.
   * This helps in reducing redundant API calls and enhances performance.
   */
  cache = inject(CacheInventoryService);

  /**
   * The Angular Router service used for navigation within the application.
   * It allows the component to navigate to different routes, such as inventory item details.
   */
  router = inject(Router);

  /**
   * A reference to the accordion components within the view.
   * This allows programmatic control to open or close all accordion sections.
   */
  @ViewChildren(AccordionComponent) accordions!: QueryList<AccordionComponent>;

  @ViewChildren(RangeSliderComponent) rangeSliders!: QueryList<RangeSliderComponent>;

  /**
   * The form group that contains all the filter controls for the inventory items.
   * It allows the user to filter items based on various criteria (cost center, price, date, etc.).
   */
  inventoryForm!: FormGroup;

  /**
   * Arrays that store filter options fetched from the cache service.
   * These are used to populate dropdowns or other selection elements for filtering inventory items.
   */
  costCenters: string[] = [];
  companies: string[] = [];
  serialNumbers: string[] = [];
  locations: string[] = [];
  orderers: string[] = [];
  tags: string[] = [];
  minAndMaxId: minAndMaxId = {} as minAndMaxId;
  minAndMaxPrice: minAndMaxPrice = {} as minAndMaxPrice;

  showDeinventoried = signal<boolean>(false);

  isFiltersVisible = signal<boolean>(true);

  /**
   * Initializes the component and sets up the filter form and data fetching.
   * This method runs when the component is initialized (`ngOnInit` lifecycle hook).
   */
  ngOnInit(): void {
    // Initializing the inventory form with empty values or default filter values
    this.inventoryForm = new FormGroup({
      costCenter: new FormControl([]),
      minId: new FormControl(''),
      maxId: new FormControl(''),
      company: new FormControl([]),
      minPrice: new FormControl(''),
      maxPrice: new FormControl(''),
      createdAfter: new FormControl(''),
      createdBefore: new FormControl(''),
      serialNumber: new FormControl([]),
      location: new FormControl([]),
      orderer: new FormControl([]),
      tags: new FormControl([]),
      isDeinventoried: new FormControl(this.showDeinventoried()),
    })

    // Assigning the form group as the filter for the server data sourc
    this.serverTableDataSource.filter = this.inventoryForm;

    // Fetching filter options from the cache service
    this.cache.getCostCenters().subscribe(costCenters => this.costCenters = costCenters);
    this.cache.getCompanies().subscribe(companies => this.companies = companies);
    this.cache.getSerialNumbers().subscribe(serialNumbers => this.serialNumbers = serialNumbers);
    this.cache.getLocations().subscribe(locations => this.locations = locations);
    this.cache.getOrderers().subscribe(orderers => this.orderers = orderers);
    this.cache.getTags().subscribe(tags => this.tags = tags);
    this.cache.getMinAndMaxPrice().subscribe(minAndMaxPrice => this.minAndMaxPrice = minAndMaxPrice);
    this.cache.getMinAndMaxId().subscribe(minAndMaxId => this.minAndMaxId = minAndMaxId);
  }


  /**
   * Opens all sections of the accordion.
   * This method loops through all accordion components and triggers the `openAll()` method to expand all sections.
   */
  openAllAccordion() {
    this.accordions.map((accordion: AccordionComponent) => {
      accordion.matAccordion.openAll();
    })
  }

  /**
   * Closes all sections of the accordion.
   * This method loops through all accordion components and triggers the `closeAll()` method to collapse all sections.
   */
  closeAllAccordions() {
    this.accordions.map((accordion: AccordionComponent) => {
      accordion.matAccordion.closeAll();
    })
  }

  navigateToDetailpageOf(id: number) {
    this.router.navigate(['/inventory', id]);
  }

  checkDeinventoriedBox(event?: KeyboardEvent) {
    if (event && event.key !== 'Enter' && event.key !== ' ') {
      return; // Only toggle on Enter or Space key
    }
    this.showDeinventoried.set(!this.showDeinventoried());
    this.inventoryForm.get('isDeinventoried')?.setValue(this.showDeinventoried());
  }

  resetFilters() {
    this.showDeinventoried.set(false);
    this.inventoryForm.reset({
      costCenter: [],
      minId: this.minAndMaxId.minId,
      maxId: this.minAndMaxId.maxId,
      company: [],
      minPrice: this.minAndMaxPrice.minPrice,
      maxPrice: this.minAndMaxPrice.maxPrice,
      createdAfter: '',
      createdBefore: '',
      serialNumber: [],
      location: [],
      orderer: [],
      tags: [],
      isDeinventoried: this.showDeinventoried(),
    });
    this.rangeSliders.forEach(slider => slider.resetSlider());
  }
}
