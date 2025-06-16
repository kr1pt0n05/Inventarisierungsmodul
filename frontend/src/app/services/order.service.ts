import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Article, Order } from '../models/Order';

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

  getOrderArticleByIds(orderId: number, articleId: number): Observable<Article | undefined> {
    return new Observable<Article | undefined>((observer) => {
      this.http.get<Order[]>(this.url).subscribe({
        next: (orders: Order[]) => {
          const article = orders
            .find(order => order.id == orderId)
            ?.articles.find(article => article.article_id == articleId);
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
    return this.http.patch<Article>(`${this.url}/${orderId}/articles/${articleId}`, article);
  }
}
