import { CollectionViewer } from '@angular/cdk/collections';
import { DataSource } from '@angular/cdk/table';
import { inject, Injectable } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort, Sort } from '@angular/material/sort';
import { BehaviorSubject, Observable } from 'rxjs';
import { Inventories } from '../models/inventories';
import { InventoryItem } from '../models/inventory-item';
import { InventoriesService } from './inventories.service';


// Interface for Pagination details (pageIndex and pageSize)
interface Page {
  pageIndex: number,
  pageSize: number,
}

// Interface for filtering options
export interface Filter {
  tags?: string[],
  minId?: number,
  maxId?: number,
  minPrice?: number,
  maxPrice?: number,
  isDeinventoried?: boolean,
  orderer?: string[],
  company?: string[],
  location?: string[],
  costCenter?: string[],
  serialNumber?: string[],
}

// Interface for query parameters used for API request
// tracking currently selected pagesize, pageindex, filters, sorting & text of searchbar
interface QueryParams {
  currentPage: Page,
  currentSort: Sort,
  currentFilter: Filter,
  currentSearchText: string,
}


/**
 * ServerTableDataSourceService
 *
 * This service is responsible for providing data to the Mat-Table component with support for
 * pagination, sorting, and filtering. It communicates with the backend API through the
 * InventoriesService to fetch the inventory items, update their state, and manage pagination
 * and sorting behavior.
 *
 * Features:
 * - Handles pagination of inventory items.
 * - Supports sorting of inventory items by various fields.
 * - Applies filtering based on user-specified criteria.
 * - Fetches inventory data from the backend API as needed.
 * - Caches pages to avoid redundant API calls.
 *
 * Properties:
 * - _data: BehaviorSubject holding the inventory items to be displayed in the table.
 * - _service: The service used to fetch data from the backend.
 * - _paginator: MatPaginator reference for handling pagination.
 * - _sorter: MatSort reference for handling sorting.
 * - _filter: FormGroup holding the active filter values.
 * - _searchbar: FormControl for managing search input text.
 * - _queryParams: BehaviorSubject holding the current query parameters (pagination, sort, filter, search).
 * - _pageCache: Cache that tracks previously loaded pages.
 *
 * Methods:
 * - ngOnInit(): Initializes the service with default parameters.
 * - set data(): Sets the inventory items to be displayed in the table.
 * - set paginator(): Handles pagination changes and updates queryParams.
 * - set sorter(): Handles sorting changes and updates queryParams.
 * - set filter(): Handles filter changes and updates queryParams.
 * - set searchbar(): Handles search text input changes and updates queryParams.
 * - fetchData(): Fetches data from the backend API using current query parameters.
 * - connect(): Provides an observable stream of the data to the MatTable.
 * - disconnect(): Disconnects the data from the MatTable.
 */
@Injectable({
  providedIn: 'root'
})

// Implements DataSource to be used by a Mat-Table
export class ServerTableDataSourceService<T> extends DataSource<T> {

  /**
   * BehaviorSubject holding the data to be displayed in the table.
   */
  private readonly _data: BehaviorSubject<any[]>;

  /**
   * The service used to fetch inventory data from the backend.
   */
  private readonly _service: InventoriesService = inject(InventoriesService);


  /**
   * Reference to the paginator for handling pagination.
   */
  private _paginator: MatPaginator | undefined;

  /**
   * Reference to the sorter for handling sorting.
   */
  private _sorter: MatSort | undefined;

  /**
   * Reference to the filter form group for handling filters.
   */
  private _filter: FormGroup | undefined;

  /**
   * Reference to the search bar input for handling search text.
   */
  private _searchbar: FormControl | undefined;

  /**
   * BehaviorSubject holding the current query parameters (pagination, sorting, filter, search).
   */
  private readonly _queryParams: BehaviorSubject<QueryParams>;

  /**
   * Cache to track previously loaded pages and avoid redundant API calls.
   */
  private _pageCache: Map<Page, any> = new Map();

  constructor() {
    super();

    // Initialize the data and query parameters
    this._data = new BehaviorSubject<InventoryItem[]>([]);
    this._queryParams = new BehaviorSubject<QueryParams>({
      currentPage: { pageIndex: 0, pageSize: 10 },
      currentSort: { active: 'id', direction: 'asc' },
      currentFilter: {},
      currentSearchText: '',
    });

    // Subscribe to queryParams and fetch data whenever the parameters change
    this._queryParams.subscribe((queryParams: QueryParams) => {
      this.fetchData(
        queryParams.currentPage.pageIndex,
        queryParams.currentPage.pageSize,
        queryParams.currentSort.active,
        queryParams.currentSort.direction,
        queryParams.currentFilter,
        queryParams.currentSearchText,
      )
    })
  }


  /**
   * Sets the data to be displayed in the table.
   * Ensures the data is an array before notifying subscribers.
   * @param data The data to be set in the table.
   */
  set data(data: any[]) {
    data = Array.isArray(data) ? data : [];
    this._data.next(data);
  }

  /**
   * Sets the paginator reference and subscribes to page changes.
   * Updates query parameters when the page changes.
   * @param paginator The MatPaginator reference to be set.
   */
  set paginator(paginator: MatPaginator) {
    this._paginator = paginator;
    this._paginator.page.subscribe((page: PageEvent) => {
      this._queryParams.next({
        ...this._queryParams.value, currentPage: { pageIndex: page.pageIndex, pageSize: page.pageSize }
      });
    })
  }

  /**
   * Sets the sorter reference and subscribes to sort changes.
   * Updates query parameters when the sort order changes.
   * @param sorter The MatSort reference to be set.
   */
  set sorter(sorter: MatSort) {
    this._sorter = sorter;
    this._sorter.sortChange.subscribe((sort: Sort) => {
      if(sort.active === 'orderer') sort.active = 'user'; // since backend returns 'orderer' but wants it back as 'user' ???
      if(sort.active === 'date') sort.active = 'createdAt'; // this is my mistake =D
      if(sort.active === 'cost_center') sort.active = 'costCenter'; // this is my mistake =D
      this._queryParams.next({ ...this._queryParams.value, currentSort: sort });
    })
  }

  /**
   * Sets the filter form group reference and subscribes to filter value changes.
   * Updates query parameters when the filter values change.
   * @param filter The FormGroup containing filter values.
   */
  set filter(filter: FormGroup) {
    this._filter = filter;
    this._filter.valueChanges.subscribe((filter: Filter) => {
      this._queryParams.next({ ...this._queryParams.value, currentFilter: filter});
    });
  }

  /**
   * Sets the search bar reference and subscribes to search text input changes.
   * Updates query parameters when the search text changes.
   * @param searchbar The FormControl containing the search text.
   */
  set searchbar(searchbar: FormControl) {
    this._searchbar = searchbar;
    this._searchbar.valueChanges.subscribe((searchText: string) => {
      this._queryParams.next({ ...this._queryParams.value, currentSearchText: searchText });
    })
  }

  /**
   * Fetches data from the backend API based on the current query parameters.
   * This method is invoked whenever pagination, sorting, filtering, or search text changes.
   * @param pageNumber The current page index.
   * @param pageSize The number of items per page.
   * @param sortActive The active sort field.
   * @param sortDirection The direction of sorting.
   * @param filter The active filter settings.
   * @param searchText The search text input from the user.
   */
  private fetchData(pageNumber: number, pageSize: number, sortActive: string, sortDirection: string, filter: Filter, searchText: string = '') {
    // Call the API to fetch the inventory data with query parameters
    this._service.getInventories(pageNumber, pageSize, sortActive, sortDirection, filter, searchText).subscribe((inventories: Inventories) => {
      if (inventories.content === undefined) {
        this.data = []; //  If no content is returned, set data to an empty array
      } else {
        // Transform the fetched data to match the table structure
        this.data = inventories.content.map((item: InventoryItem) => ({
          id: item.id,
          description: item.description,
          company: item.company,
          price: item.price,
          createdAt: item.created_at,
          serialNumber: item.serial_number,
          location: item.location,
          orderer: item.orderer,
          cost_center: item.cost_center,
        }));
      }
      // Update the total item count for paginator
      if (this._paginator) this._paginator.length = inventories.totalElements;
    })
  }


  // This method is used by MatTable to connect to the data source
  connect(collectionViewer: CollectionViewer): Observable<T[]> {
    return this._data;
  }

  // This method is used by MatTable to disconnect from the data source
  disconnect(collectionViewer: CollectionViewer): void {
    this._data.complete();
  }

}
