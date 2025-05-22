import {inject, Injectable} from '@angular/core';
import {DataSource} from '@angular/cdk/table';
import {CollectionViewer} from '@angular/cdk/collections';
import {BehaviorSubject, Observable} from 'rxjs';
import {InventoriesService} from './inventories.service';
import {Inventories} from '../models/inventories';
import {InventoryItem} from '../models/inventory-item';

@Injectable({
  providedIn: 'root'
})
export class ServerTableDataSourceService<T> extends DataSource<T> {

  // Data obtained by an API and passed to Mat-Table
  private readonly _data: BehaviorSubject<any[]>;

  // API
  private _service: InventoriesService  = inject(InventoriesService);


  constructor() {
    super();

    // Initialize data with an empty array
    this._data = new BehaviorSubject<InventoryItem[]>([]);

    // Fetch Inventories, transform them and notify subscribers
    this._service.getInventories(1, 50).subscribe((inventories: Inventories) => {
      this.data = inventories.content.map((item: InventoryItem) => ({
        id: item.id,
        user: item.user.name,
        description: item.description,
        company: item.company.name,
        price: item.price,
        createdAt: new Date(`2025-01-01T${item.createdAt}`),
        serialNumber: item.serialNumber,
        location: item.location,
        orderer: item.user.name,
      }));
    })
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


  // Used by the MatTable. Called when it connects to the data source.
  connect(collectionViewer: CollectionViewer): Observable<T[]> {
    return this._data;
  }


  // Used by the MatTable. Called when it disconnects from the data source.
  disconnect(collectionViewer: CollectionViewer): void {
  }
}
