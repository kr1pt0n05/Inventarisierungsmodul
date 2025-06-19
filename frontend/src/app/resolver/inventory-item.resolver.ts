import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  Router,
  RouterStateSnapshot
} from '@angular/router';
import { catchError, EMPTY, map, Observable } from 'rxjs';
import { InventoryItem } from '../models/inventory-item';
import { InventoriesService } from '../services/inventories.service';

@Injectable({
  providedIn: 'root'
})

export class InventoryItemResolver implements Resolve<InventoryItem> {
  debug = false; // Set to true to use mock data
  constructor(private readonly inventoriesService: InventoriesService,
    private readonly router: Router) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<InventoryItem> {
    return this.inventoriesService.getInventoryById(parseInt(route.paramMap.get('id')!)).pipe(
      map(inventoryItem => inventoryItem),
      catchError(error => {
        this.router.navigate(['/404'], { skipLocationChange: true });
        return EMPTY;
      })
    );
  }
}
