import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {Order} from '../models/Order';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private readonly url = 'http://localhost:8080/orders'

  constructor(private readonly http: HttpClient) {
  }

  getOpenOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(this.url);
  }
}
