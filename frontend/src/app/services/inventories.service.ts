import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { Comment } from '../models/comment';
import { Extension } from '../models/extension';
import { Inventories } from '../models/inventories';
import { InventoryItem } from '../models/inventory-item';



@Injectable({
  providedIn: 'root'
})
export class InventoriesService {
  private readonly url = 'http://localhost:8080/inventories'

  constructor(private readonly http: HttpClient) {
    this.http = http;
  }

  getInventories(pageNumber: number, pageSize: number): Observable<Inventories> {
    const params = {
      'page': pageNumber,
      'size': pageSize,
    }
    return this.http.get<Inventories>(this.url, { params: params });
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

}
