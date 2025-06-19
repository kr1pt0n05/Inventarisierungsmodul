import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  Router,
  RouterStateSnapshot
} from '@angular/router';
import { catchError, map, Observable } from 'rxjs';
import { Extension } from '../models/extension';
import { InventoriesService } from '../services/inventories.service';

@Injectable({
  providedIn: 'root'
})
export class ExtensionResolver implements Resolve<Extension> {
  constructor(private readonly inventoriesService: InventoriesService,
    private readonly router: Router) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Extension> {
    return this.inventoriesService.getExtensionById(parseInt(route.paramMap.get('inventoryId')!), parseInt(route.paramMap.get('extensionId')!)).pipe(
      map(extension => extension),
      catchError(error => {
        this.router.navigate(['/404'], { skipLocationChange: true });
        return EMPTY;
      })
    );
  }
}
