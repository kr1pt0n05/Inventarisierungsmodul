import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import {map, Observable} from 'rxjs';
import { Comment } from '../models/comment';
import { Extension } from '../models/extension';
import { Inventories } from '../models/inventories';
import { InventoryItem } from '../models/inventory-item';
import {Filter} from './server-table-data-source.service';


@Injectable({
  providedIn: 'root'
})
export class InventoriesService {
  private readonly url = 'http://localhost:8080/inventories'

  constructor(private readonly http: HttpClient) { }

  getInventories(pageNumber: number, pageSize: number, sortActive: string, sortDirection: string, filter: Filter): Observable<Inventories> {
    const params: any = {
      'page': pageNumber,
      'size': pageSize,
      'orderBy': sortActive,
      'direction': sortDirection,
    }

    // Append filter fields to params if they are defined
    Object.entries(filter).forEach(([key, value]) => {
      if (value !== undefined && value !== null) {
        // Convert arrays to comma-separated strings
        params[key] = Array.isArray(value) ? value.join(',') : value;
      }
    });

    return this.http.get<Inventories>(this.url, {params: params});
  }

  getInventoryById(id: number): Observable<InventoryItem> {
    return this.http.get<InventoryItem>(`${this.url}/${id}`);
  }

  getCommentsForId(id: number): Observable<Comment[]> {
    return this.http.get<any>(`${this.url}/${id}/comments`);
  }

  getExtensionsForId(id: number): Observable<Extension[]> {
    return this.http.get<any>(`${this.url}/${id}/extensions`);
  }

  getChangesForId(id: number): Observable<any> {
    return this.http.get<any>(`${this.url}/${id}/changes`);
  }

  getAllCostCenters(): Observable<string[]> {
    return this.http.get<any>(`${this.url}/costCenters`).pipe(
      map(response => response.costCenters)
    );
  }

  getAllCompanies(): Observable<string[]> {
    return this.http.get<any>(`${this.url}/companies`).pipe(
      map(response => response.companies)
    );
  }

  getAllSerialNumbers(): Observable<string[]> {
    return this.http.get<any>(`${this.url}/serialNumbers`);
  }

  getAllLocations(): Observable<string[]> {
    return this.http.get<any>(`${this.url}/locations`);
  }

  getAllOrderers(): Observable<string[]> {
    return this.http.get<any>(`${this.url}/orderers`).pipe(
      map(response => response.orderers)
    );
  }

  getAlltags(): Observable<string[]> {
    return this.http.get<any>(`${this.url}/tags`);
  }

}
