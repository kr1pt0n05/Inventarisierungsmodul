import { CollectionViewer } from '@angular/cdk/collections';
import { DataSource } from '@angular/cdk/table';
import { inject, Injectable } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort, Sort } from '@angular/material/sort';
import { BehaviorSubject, Observable } from 'rxjs';
import { Inventories } from '../models/inventories';
import { InventoryItem } from '../models/inventory-item';
import { InventoriesService } from './inventories.service';
import {FormControl, FormGroup} from '@angular/forms';


interface Page{
  pageIndex: number,
  pageSize: number,
}

// Export this, since the FormGroup, that is passed into this service, should implement that Interface too.
export interface Filter{
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

interface QueryParams{
  currentPage: Page,
  currentSort: Sort,
  currentFilter: Filter,
  currentSearchText: string,
}


@Injectable({
  providedIn: 'root'
})
export class ServerTableDataSourceService<T> extends DataSource<T> {

  // Data obtained by an API and passed to a Mat-Table.
  private readonly _data: BehaviorSubject<any[]>;

  // API to call, with corresponding Query parameters, if any of Paginator, Sorter or Filter changes.
  private _service: InventoriesService = inject(InventoriesService);

  // Paginator
  private _paginator: MatPaginator | undefined;

  // Sorter
  private _sorter: MatSort | undefined;

  // Filter
  private _filter: FormGroup | undefined;

  // User entered input from search bar
  private _searchbar: FormControl | undefined;

  // Store currently selected Page, Sort & Filter as Query parameters for API
  private readonly _queryParams: BehaviorSubject<QueryParams>;


  // Cache to track loaded pages and their corresponding page sizes.
  // When the paginator navigates to a page, it checks this cache.
  // If the page exists, it slices `_data` and assigns it to `_paginateData` for use in the Mat-Table.
  // If the page is not cached, a new API call will be triggered to fetch the data.
  private _pageCache: Map<Page, any> = new Map();

  constructor() {
    super();

    // Initialize data with an empty array
    this._data = new BehaviorSubject<InventoryItem[]>([]);
    this._queryParams = new BehaviorSubject<QueryParams>({
      currentPage: {pageIndex: 0, pageSize: 10},
      currentSort: {active: 'id', direction: 'asc'},
      currentFilter: {},
      currentSearchText: '',
    });

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


  // Update _data, check if it is an Array beforehand and notify subscribers.
  set data(data: any[]) {
    data = Array.isArray(data) ? data : [];
    console.log('Setting data:', data); // Add this
    this._data.next(data);
  }

  // Assign paginator
  // Subscribe to its PageEvent and call API with selected pages, every time user changes pages.
  set paginator(paginator: MatPaginator) {
    this._paginator = paginator;
    this._paginator.page.subscribe((page: PageEvent) => {
      this._queryParams.next({...this._queryParams.value, currentPage: {pageIndex: page.pageIndex, pageSize: page.pageSize}
      });
    })
  }

  set sorter(sorter: MatSort) {
    this._sorter = sorter;
    this._sorter.sortChange.subscribe((sort: Sort) => {
      this._queryParams.next({...this._queryParams.value, currentSort: sort});
    })
  }

  set filter(filter: FormGroup) {
    this._filter = filter;
    this._filter.valueChanges.subscribe((filter: Filter) => {
      this._queryParams.next({...this._queryParams.value, currentFilter: filter});
    });
  }

  set searchbar(searchbar: FormControl) {
    this._searchbar = searchbar;
    this._searchbar.valueChanges.subscribe((searchText: string) => {
      this._queryParams.next({...this._queryParams.value, currentSearchText: searchText});
    })
  }

  // Fetch Inventories from Inventory API & transform it to fit Mat-Table
  private fetchData(pageNumber: number, pageSize: number, sortActive: string, sortDirection: string, filter: Filter, searchText: string = '') {
    this._service.getInventories(pageNumber, pageSize, sortActive, sortDirection, filter, searchText).subscribe((inventories: Inventories) => {
      if (inventories.content === undefined) {
        this.data = [];
      } else {
        this.data = inventories.content.map((item: InventoryItem) => ({
          id: item.id,
          description: item.description,
          company: item.company,
          price: item.price,
          createdAt: item.createdAt,
          serialNumber: item.serialNumber,
          location: item.location,
          orderer: item.orderer,
        }));
      }
      if (this._paginator) this._paginator.length = inventories.totalElements;
    })
  }


  // Used by the MatTable. Called when it connects to the data source.
  connect(collectionViewer: CollectionViewer): Observable<T[]> {
    return this._data;
  }

  // Used by the MatTable. Called when it disconnects from the data source.
  disconnect(collectionViewer: CollectionViewer): void {
    this._data.complete();
  }

}
