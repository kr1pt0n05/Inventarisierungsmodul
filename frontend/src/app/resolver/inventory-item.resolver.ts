import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot
} from '@angular/router';
import { Observable, of } from 'rxjs';
import { InventoryItem } from '../models/inventory-item';
import { InventoriesService } from '../services/inventories.service';

@Injectable({
  providedIn: 'root'
})

export class InventoryItemResolver implements Resolve<InventoryItem> {
  debug = false; // Set to true to use mock data
  constructor(private readonly inventoriesService: InventoriesService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<InventoryItem> {
    let inventoryItem = null;

    if (this.debug) {
      inventoryItem = of({
        costCenter: "123",
        id: parseInt(route.paramMap.get('id')!),
        description: 'Artikelbeschreibung',
        company: 'firma',
        price: 1.99,
        createdAt: '27.05.2023',
        serialNumber: '0987654321',
        location: 'F1.312',
        orderer: 'Name',
        tags: [
          { id: 1, name: 'Laptop' },
          { id: 2, name: 'Monitor' },
          { id: 3, name: 'Zubehör' }
        ],
      } as InventoryItem);
    } else {
      inventoryItem = this.inventoriesService.getInventoryById(parseInt(route.paramMap.get('id')!));
    }

    return inventoryItem;
  }
}
