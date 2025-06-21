import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  Router,
  RouterStateSnapshot
} from '@angular/router';
import { catchError, map, Observable, of } from 'rxjs';
import { Extension } from '../models/extension';
import { InventoriesService } from '../services/inventories.service';

@Injectable({
  providedIn: 'root'
})
export class ExtensionsResolver implements Resolve<Extension[]> {
  constructor(private readonly inventoriesService: InventoriesService,
    private readonly router: Router) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Extension[]> {
    return this.inventoriesService.getExtensionsForId(parseInt(route.paramMap.get('id')!)).pipe(
      map(inventoryItem => inventoryItem),
      catchError(error => {
        return of([]) as Observable<Extension[]>;
      })
    );;
  }
}
