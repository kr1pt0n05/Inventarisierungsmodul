import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Inventories} from '../models/inventories';
import {Filter} from './server-table-data-source.service';


@Injectable({
  providedIn: 'root'
})
export class InventoriesService {
  private readonly url = 'http://localhost:8080/inventories'

  constructor(private readonly http: HttpClient) {
  }

  getInventories(pageNumber: number, pageSize: number, sortActive: string, sortDirection: string, filter: Filter): Observable<Inventories> {
    const params: any = {
      'page': pageNumber,
      'size': pageSize,
      'orderBy': sortActive,
      'direction': sortDirection,
    }

    // Append filter fields to params if they are defined
    Object.entries(filter).forEach(([key, value]) => {
      if (value !== undefined && value !== null) {
        // Convert arrays to comma-separated strings
        params[key] = Array.isArray(value) ? value.join(',') : value;
      }
    });

    return this.http.get<Inventories>(this.url, {params: params});
  }

}
