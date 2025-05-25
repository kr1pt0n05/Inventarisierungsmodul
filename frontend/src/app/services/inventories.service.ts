import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Inventories } from '../models/inventories';
import { InventoryItem } from '../models/inventory-item';

@Injectable({
  providedIn: 'root'
})
export class InventoriesService {

  constructor(private readonly http: HttpClient) {
    this.http = http;
  }

  getInventories(): Observable<Inventories> {
    return this.http.get<Inventories>(`http://localhost:8080/inventories`);
  }

  getInventoryById(id: number): Observable<InventoryItem> {
    return this.http.get<InventoryItem>(`http://localhost:8080/inventories/${id}`);
  }
}
