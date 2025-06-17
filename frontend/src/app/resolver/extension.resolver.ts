import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot
} from '@angular/router';
import { Observable } from 'rxjs';
import { Extension } from '../models/extension';
import { InventoriesService } from '../services/inventories.service';

@Injectable({
  providedIn: 'root'
})
export class ExtensionResolver implements Resolve<Extension> {
  constructor(private readonly inventoriesService: InventoriesService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Extension> {
    return this.inventoriesService.getExtensionById(parseInt(route.paramMap.get('inventoryId')!), parseInt(route.paramMap.get('extensionId')!));
  }
}
