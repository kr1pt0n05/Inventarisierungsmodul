import { Injectable } from '@angular/core';
import {InventoriesService} from './inventories.service';
import {Observable, of, tap} from 'rxjs';
import {minAndMaxId, minAndMaxPrice} from '../pages/inventory/inventory.component';


/**
 * CacheInventoryService
 *
 * This service handles caching of inventory-related data (e.g., cost centers, companies, serial numbers, etc.)
 * to avoid redundant API calls. It interacts with the `InventoriesService` to fetch data, but caches the data
 * after the first request so that subsequent requests for the same data can be served from the cache.
 * This improves performance by reducing the number of HTTP requests made to the backend.
 *
 * Features:
 * - Caches the results of inventory-related API calls (cost centers, companies, etc.).
 * - If data is already cached, serves it from memory instead of making an API request.
 * - Updates the cache when new data is fetched from the backend.
 *
 * Dependencies:
 * - `InventoriesService`: The service responsible for fetching the data from the backend.
 * - `RxJS`: Used for working with observables and handling asynchronous operations.
 */
@Injectable({
  providedIn: 'root'
})
export class CacheInventoryService {
  // Cache storage using a Map to store data keyed by identifiers.
  private readonly cache = new Map<string, any>();

  /**
   * Constructor that injects the InventoriesService for making inventory-related API requests.
   *
   * @param inventoriesService - The InventoriesService for fetching inventory data from the backend.
   */
  constructor(private readonly inventoriesService: InventoriesService) {
  }

  /**
   * Retrieves the list of cost centers from the cache if available.
   * If the data is not in the cache, it fetches it from the API and then caches it.
   *
   * @returns {Observable<string[]>} - An observable containing an array of cost centers.
   */
  getCostCenters(): Observable<string[]> {
    if (this.cache.has('costCenters')) {
      return of(this.cache.get('costCenters') as string[]);
    } else {
      return this.inventoriesService.getAllCostCenters().pipe(
        tap(data => this.cache.set('costCenters', data))
      );
    }
  }

  /**
   * Retrieves the list of companies from the cache if available.
   * If the data is not in the cache, it fetches it from the API and then caches it.
   *
   * @returns {Observable<string[]>} - An observable containing an array of companies.
   */
  getCompanies(): Observable<string[]> {
    if (this.cache.has('companies')) {
      return of(this.cache.get('companies') as string[]);
    } else {
      return this.inventoriesService.getAllCompanies().pipe(
        tap(data => this.cache.set('companies', data))
      );
    }
  }

  /**
   * Retrieves the list of serial numbers from the cache if available.
   * If the data is not in the cache, it fetches it from the API and then caches it.
   *
   * @returns {Observable<string[]>} - An observable containing an array of serial numbers.
   */
  getSerialNumbers(): Observable<string[]> {
    if (this.cache.has('serialNumbers')) {
      return of(this.cache.get('serialNumbers') as string[]);
    } else {
      return this.inventoriesService.getAllSerialNumbers().pipe(
        tap(data => this.cache.set('serialNumbers', data))
      );
    }
  }

  /**
   * Retrieves the list of locations from the cache if available.
   * If the data is not in the cache, it fetches it from the API and then caches it.
   *
   * @returns {Observable<string[]>} - An observable containing an array of locations.
   */
  getLocations(): Observable<string[]> {
    if (this.cache.has('locations')) {
      return of(this.cache.get('locations') as string[]);
    } else {
      return this.inventoriesService.getAllLocations().pipe(
        tap(data => this.cache.set('locations', data))
      );
    }
  }

  /**
   * Retrieves the list of orderers from the cache if available.
   * If the data is not in the cache, it fetches it from the API and then caches it.
   *
   * @returns {Observable<string[]>} - An observable containing an array of orderers.
   */
  getOrderers(): Observable<string[]> {
    if (this.cache.has('orderers')) {
      return of(this.cache.get('orderers') as string[]);
    } else {
      return this.inventoriesService.getAllOrderers().pipe(
        tap(data => this.cache.set('orderers', data))
      );
    }
  }

  /**
   * Retrieves the list of tags from the cache if available.
   * If the data is not in the cache, it fetches it from the API and then caches it.
   *
   * @returns {Observable<string[]>} - An observable containing an array of tags.
   */
  getTags(): Observable<string[]> {
    if (this.cache.has('tags')) {
      return of(this.cache.get('tags') as string[]);
    } else {
      return this.inventoriesService.getAlltags().pipe(
        tap(data => this.cache.set('tags', data))
      );
    }
  }


  getMinAndMaxId(): Observable<minAndMaxId> {
    if (this.cache.has('minAndMaxIds')) {
      return of(this.cache.get('minAndMaxIds') as minAndMaxId);
    } else {
      return this.inventoriesService.getMinAndMaxId().pipe(
        tap(data => this.cache.set('minAndMaxIds', data))
      )
    }
  }

  getMinAndMaxPrice(): Observable<minAndMaxPrice> {
    if (this.cache.has('minAndMaxPrice')) {
      return of(this.cache.get('minAndMaxPrice') as minAndMaxPrice);
    }else {
      return this.inventoriesService.getMinAndMaxPrice().pipe(
        tap(data => this.cache.set('minAndMaxPrice', data))
      )
    }
  }

}
