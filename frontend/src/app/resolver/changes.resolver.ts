import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot
} from '@angular/router';
import { catchError, Observable, of } from 'rxjs';
import { Change } from '../models/change';
import { InventoriesService } from '../services/inventories.service';

@Injectable({
  providedIn: 'root'
})
export class InventoryItemChangesResolver implements Resolve<Change[]> {
  constructor(private readonly invetoriesService: InventoriesService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Change[]> {
    return this.invetoriesService.getChangesForId(parseInt(route.paramMap.get('id')!)).pipe(
      (changes) => changes,
      catchError(error => {
        return of([]) as Observable<Change[]>;
      })
    );
  }
}
