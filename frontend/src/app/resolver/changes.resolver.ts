import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot
} from '@angular/router';
import { Observable, of } from 'rxjs';
import { Change } from '../models/change';

@Injectable({
  providedIn: 'root'
})
export class InventoryItemChangesResolver implements Resolve<Change[]> {
  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Change[]> {
    const changes = [
      {
        date: '2023-10-01',
        inventoryNumber: parseInt(route.paramMap.get('id')!),
        changedBy: 'Max Mustermann',
        changedTable: 'extensions',
        changedColumn: 'price',
        oldValue: '100.00',
        newValue: '120.00'
      },
      {
        date: '2023-10-02',
        inventoryNumber: parseInt(route.paramMap.get('id')!),
        changedBy: 'Erika Musterfrau',
        changedTable: 'inventory_items',
        changedColumn: 'location',
        oldValue: 'F1.312',
        newValue: 'F1.313'
      }
    ];
    return of(changes);
  }
}
