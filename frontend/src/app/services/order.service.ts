import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environment';
import { Article } from "../models/Article";
import { Order } from '../models/Order';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private readonly url = `${environment.apiUrl}/orders`;

  constructor(private readonly http: HttpClient) {
  }

  getOpenOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(this.url);
  }

  getOrderArticleByIds(orderId: number, articleId: number): Observable<Article> {
    return new Observable<Article>((observer) => {
      this.http.get<Order[]>(this.url).subscribe({
        next: (orders: Order[]) => {
          const article = orders
            .find(order => order.id == orderId)
            ?.articles.find(article => article.article_id == articleId);
          if (article === undefined) {
            console.log(orders, orderId, articleId);
            observer.error(new Error(`Article with ID ${articleId} not found in order ${orderId}`));
            return;
          }
          observer.next(article);
          observer.complete();
        },
        error: (error) => {
          observer.error(error);
        }
      });
    });
  }

  updateOrderArticle(orderId: number, articleId: number, article: Article): Observable<Article> {
    return this.http.patch<Article>(`${this.url}/${orderId}/items/${articleId}`, article);
  }

  getOrderById(id: number): Observable<Order> {
    return this.http.get<Order>(`${this.url}/${id}`);
  }

  // OrderId hardcoded, since it doesnt matter.
  getArticleById(id: number): Observable<Article> {
    return this.http.get<Article>(`${this.url}/1/items/${id}`);
  }
}
