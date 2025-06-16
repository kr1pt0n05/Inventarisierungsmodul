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
    const debug = false; // Set to true to use mock data
    if (!debug) {
      return this.invetoriesService.getChangesForId(parseInt(route.paramMap.get('id')!));
    } else {
      console.warn('Using mock data for changes resolver');

      return of([
        {
          id: 1,
          createdAt: '2023-10-01T10:00:00Z',
          changedBy: 'Max Mustermann',
          changedTable: 'extensions',
          changedColumn: 'price',
          attributeChanged: 'price',
          valueFrom: '100.00',
          valueTo: '120.00'
        },
        {
          id: 2,
          createdAt: '2023-10-02T11:00:00Z',
          changedBy: 'Erika Musterfrau',
          changedTable: 'inventory_items',
          changedColumn: 'location',
          attributeChanged: 'location',
          valueFrom: 'F1.312',
          valueTo: 'F1.313'
        }
      ]);
    }
  }
}
