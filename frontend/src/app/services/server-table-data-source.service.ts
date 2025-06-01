import { CollectionViewer } from '@angular/cdk/collections';
import { DataSource } from '@angular/cdk/table';
import { inject, Injectable } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { BehaviorSubject, Observable } from 'rxjs';
import { Inventories } from '../models/inventories';
import { InventoryItem } from '../models/inventory-item';
import { InventoriesService } from './inventories.service';

interface Page {
  pageIndex: number,
  pageSize: number,
}

@Injectable({
  providedIn: 'root'
})
export class ServerTableDataSourceService<T> extends DataSource<T> {


  // Data obtained by an API. Containts current page and +-2 prefetched Pages.
  private readonly _data: BehaviorSubject<any[]>;


  // API
  private _service: InventoriesService = inject(InventoriesService);

  // Paginator
  private _paginator: MatPaginator | undefined;

  // Cache to track loaded pages and their corresponding page sizes.
  // When the paginator navigates to a page, it checks this cache.
  // If the page exists, it slices `_data` and assigns it to `_paginateData` for use in the Mat-Table.
  // If the page is not cached, a new API call will be triggered to fetch the data.
  private _pageCache: Map<Page, any> = new Map();

  constructor() {
    super();

    // Initialize data with an empty array
    this._data = new BehaviorSubject<InventoryItem[]>([]);

    // Fetch Inventories & notify subscribers
    this.fetchData(1, 10);
  }

  // Data to be rendered by Mat-Table.
  get data(): BehaviorSubject<any[]> {
    return this._data;
  }

  // Update _data, check if it is an Array beforehand and notify subscribers.
  set data(data: any[]) {
    data = Array.isArray(data) ? data : [];
    this._data.next(data);
  }

  // Assign paginator
  // Subscribe to its PageEvent and call API with selected pages, every time user changes pages.
  set paginator(paginator: MatPaginator) {
    this._paginator = paginator;
    this._paginator.page.subscribe((page: PageEvent) => {
      this.fetchData(page.pageIndex, page.pageSize);
    })
  }

  // Fetch Inventories from Inventory API & transform it to fit Mat-Table
  private fetchData(pageNumber: number, pageSize: number) {
    this._service.getInventories(pageNumber, pageSize).subscribe((inventories: Inventories) => {
      this.data = inventories.content.map((item: InventoryItem) => ({
        id: item.id,
        description: item.description,
        company: item.company,
        price: item.price,
        createdAt: new Date(`2025-01-01T${item.createdAt}`),
        serialNumber: item.serialNumber,
        location: item.location,
        orderer: item.orderer,
      }));
      if (this._paginator) this._paginator.length = inventories.totalElements;
    })
  }


  // Used by the MatTable. Called when it connects to the data source.
  connect(collectionViewer: CollectionViewer): Observable<T[]> {
    return this._data;
  }

  // Used by the MatTable. Called when it disconnects from the data source.
  disconnect(collectionViewer: CollectionViewer): void {
  }

}
