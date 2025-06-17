import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot
} from '@angular/router';
import { Observable } from 'rxjs';
import { InventoryItem } from '../models/inventory-item';
import { InventoriesService } from '../services/inventories.service';

@Injectable({
  providedIn: 'root'
})

export class InventoryItemResolver implements Resolve<InventoryItem> {
  debug = false; // Set to true to use mock data
  constructor(private readonly inventoriesService: InventoriesService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<InventoryItem> {
    return this.inventoriesService.getInventoryById(parseInt(route.paramMap.get('id')!));
  }
}
