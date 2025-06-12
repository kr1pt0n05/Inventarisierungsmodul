import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {Order} from '../models/Order';
import {Article} from "../models/Article";

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

  getOrderById(id: number): Observable<Order> {
    return this.http.get<Order>(`${this.url}/${id}`);
  }

  // OrderId hardcoded, since it doesnt matter.
  getArticleById(id: number): Observable<Article> {
    return this.http.get<Article>(`${this.url}/1/items/${id}`);
  }
}
