import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {InventoryItem} from '../models/inventory-item';

@Injectable({
  providedIn: 'root'
})
export class InventoriesService {

  constructor(private http: HttpClient) {
    this.http = http;
  }

  getInventories(): Observable<InventoryItem[]> {
    return this.http.get<InventoryItem[]>(`http://localhost:8080/inventories`);
  }
}
