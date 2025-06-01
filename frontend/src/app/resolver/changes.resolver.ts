import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot
} from '@angular/router';
import { Observable, of } from 'rxjs';
import { Change } from '../models/change';
import { InventoriesService } from '../services/inventories.service';

@Injectable({
  providedIn: 'root'
})
export class InventoryItemChangesResolver implements Resolve<Change[]> {
  constructor(private readonly invetoriesService: InventoriesService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Change[]> {
    let changes: Observable<Change[]>;
    if (false) { // Set to false to use mock data

    } else {
      console.warn('Using mock data for changes resolver');

      changes = of([
        {
          id: 1,
          changedAt: '2023-10-01',
          inventoryNumber: parseInt(route.paramMap.get('id')!),
          changedBy: 'Max Mustermann',
          changedTable: 'extensions',
          changedColumn: 'price',
          oldValue: '100.00',
          newValue: '120.00'
        },
        {
          id: 2,
          changedAt: '2023-10-02',
          inventoryNumber: parseInt(route.paramMap.get('id')!),
          changedBy: 'Erika Musterfrau',
          changedTable: 'inventory_items',
          changedColumn: 'location',
          oldValue: 'F1.312',
          newValue: 'F1.313'
        }
      ]);
    }

    return changes;
  }
}
