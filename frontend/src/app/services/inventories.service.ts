import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import {map, Observable} from 'rxjs';
import { Comment } from '../models/comment';
import { Extension } from '../models/extension';
import { Inventories } from '../models/inventories';
import { InventoryItem } from '../models/inventory-item';
import { Filter } from './server-table-data-source.service';


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

    return this.http.get<Inventories>(this.url, { params: params });
  }

  // Inventory Item
  getInventoryById(id: number): Observable<InventoryItem> {
    return this.http.get<InventoryItem>(`${this.url}/${id}`);
  }

  addInventoryItem(item: InventoryItem) {
    return this.http.post<InventoryItem>(this.url, item);
  }

  updateInventoryById(id: number, item: InventoryItem) {
    return this.http.patch<InventoryItem>(`${this.url}/${id}`, item);
  }

  // Comments
  getCommentsForId(id: number): Observable<Comment[]> {
    return this.http.get<any>(`${this.url}/${id}/comments`);
  }

  addCommentToId(id: number, comment: Comment): Observable<Comment> {
    return this.http.post<Comment>(`${this.url}/${id}/comments`, comment);
  }

  deleteCommentFromId(id: number, commentId: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}/comments/${commentId}`);
  }

  // Extensions
  getExtensionsForId(id: number): Observable<Extension[]> {
    return this.http.get<any>(`${this.url}/${id}/extensions`);
  }

  // Changes
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
    return this.http.get<any>(`${this.url}/serialNumbers`).pipe(
      map(response => response.serialNumbers)
    );
  }

  getAllLocations(): Observable<string[]> {
    return this.http.get<any>(`${this.url}/locations`).pipe(
      map(response => response.locations)
    );
  }

  getAllOrderers(): Observable<string[]> {
    return this.http.get<any>(`${this.url}/orderers`).pipe(
      map(response => response.orderers)
    );
  }

  getAlltags(): Observable<string[]> {
    return this.http.get<any>(`${this.url}/tags`).pipe(
      map(response => response.tags)
    );
  }

}
