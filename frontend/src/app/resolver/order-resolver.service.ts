import { Injectable } from '@angular/core';
import {Article} from "../models/Article";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {OrderService} from "../services/order.service";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class OrderResolverService implements Resolve<Article>{

  constructor(private readonly orderService: OrderService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Article> {
    const id = route.paramMap.get('id')!;
    return this.orderService.getArticleById(parseInt(id));
  }
}
