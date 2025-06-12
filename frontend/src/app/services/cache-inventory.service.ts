import { Injectable } from '@angular/core';
import {InventoriesService} from './inventories.service';
import {Observable, of, tap} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CacheInventoryService {
  private readonly cache = new Map<string, any>();

  constructor(private readonly inventoriesService: InventoriesService) {
  }

  // Retrieve costCenters from Cache.
  // If they are null, fetch from the API and put to array
  // else retrieve from array
  getCostCenters(): Observable<string[]> {
    if (this.cache.has('costCenters')) {
      return of(this.cache.get('costCenters') as string[]);
    } else {
      return this.inventoriesService.getAllCostCenters().pipe(
        tap(data => this.cache.set('costCenters', data))
      );
    }
  }

  getCompanies(): Observable<string[]> {
    if (this.cache.has('companies')) {
      return of(this.cache.get('companies') as string[]);
    } else {
      return this.inventoriesService.getAllCompanies().pipe(
        tap(data => this.cache.set('companies', data))
      );
    }
  }

  getSerialNumbers(): Observable<string[]> {
    if (this.cache.has('serialNumbers')) {
      return of(this.cache.get('serialNumbers') as string[]);
    } else {
      return this.inventoriesService.getAllSerialNumbers().pipe(
        tap(data => this.cache.set('serialNumbers', data))
      );
    }
  }

  getLocations(): Observable<string[]> {
    if (this.cache.has('locations')) {
      return of(this.cache.get('locations') as string[]);
    } else {
      return this.inventoriesService.getAllLocations().pipe(
        tap(data => this.cache.set('locations', data))
      );
    }
  }

  getOrderers(): Observable<string[]> {
    if (this.cache.has('orderers')) {
      return of(this.cache.get('orderers') as string[]);
    } else {
      return this.inventoriesService.getAllOrderers().pipe(
        tap(data => this.cache.set('orderers', data))
      );
    }
  }

  getTags(): Observable<string[]> {
    if (this.cache.has('tags')) {
      return of(this.cache.get('tags') as string[]);
    } else {
      return this.inventoriesService.getAlltags().pipe(
        tap(data => this.cache.set('tags', data))
      );
    }
  }

}
