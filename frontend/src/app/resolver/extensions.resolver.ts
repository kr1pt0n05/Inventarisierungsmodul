import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot
} from '@angular/router';
import { Observable, of } from 'rxjs';
import { InventoryExtension } from '../models/inventory-extension';

@Injectable({
  providedIn: 'root'
})
export class ExtensionsResolver implements Resolve<InventoryExtension[]> {
  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<InventoryExtension[]> {
    return of([] as InventoryExtension[]);
  }
}
