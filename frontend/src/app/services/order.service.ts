import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable, Subject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../environment';
import { Article } from "../models/Article";
import { Order } from '../models/Order';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private readonly url = `${environment.apiUrl}/orders`;
  openArticlesChanged: Subject<void> = new Subject<void>();

  constructor(private readonly http: HttpClient) {
  }

  getOpenOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(this.url);
  }

  getOrderArticleByIds(orderId: number, articleId: number): Observable<Article> {
    return this.http.get<Article>(`${this.url}/${orderId}/items/${articleId}`);
  }

  updateOrderArticle(orderId: number, articleId: number, article: Article): Observable<Article> {
    return this.http.patch<Article>(`${this.url}/${orderId}/items/${articleId}`, article).pipe(
      tap(() => this.openArticlesChanged.next())
    );
  }

  getOrderById(id: number): Observable<Order> {
    return this.http.get<Order>(`${this.url}/${id}`);
  }

  // OrderId hardcoded, since it doesnt matter.
  getArticleById(id: number): Observable<Article> {
    return this.http.get<Article>(`${this.url}/1/items/${id}`);
  }

  getNumberOfOpenArticles(): Observable<number> {
    return this.getOpenOrders().pipe(
      map(orders => {
        const articles = orders.map(order => order.articles).flat();
        return articles.filter(article => !article.is_inventoried).length;
      })
    );
  }
}
