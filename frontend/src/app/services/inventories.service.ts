import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Inventories} from '../models/inventories';


@Injectable({
  providedIn: 'root'
})
export class InventoriesService {
  private url = 'http://localhost:8080/inventories'

  constructor(private http: HttpClient) {
  }

  getInventories(pageNumber: number, pageSize: number): Observable<Inventories> {
    const params = {
      'page': pageNumber,
      'size': pageSize,
    }
    return this.http.get<Inventories>(this.url, {params: params});
  }
}
