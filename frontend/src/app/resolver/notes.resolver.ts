import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot
} from '@angular/router';
import { Observable, of } from 'rxjs';
import { InventoryItemNotes } from '../models/inventory-item-notes';

@Injectable({
  providedIn: 'root'
})
export class InventoryItemNotesResolver implements Resolve<InventoryItemNotes[]> {
  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<InventoryItemNotes[]> {
    return of([] as InventoryItemNotes[]);
  }
}
