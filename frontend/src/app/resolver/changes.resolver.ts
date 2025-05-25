import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot
} from '@angular/router';
import { Observable, of } from 'rxjs';
import { InventoryItemChange } from '../models/inventory-item-change';

@Injectable({
  providedIn: 'root'
})
export class InventoryItemChangesResolver implements Resolve<InventoryItemChange[]> {
  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<InventoryItemChange[]> {
    return of([] as InventoryItemChange[]);
  }
}
