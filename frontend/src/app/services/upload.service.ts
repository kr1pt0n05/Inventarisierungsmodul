import { Injectable } from '@angular/core';
import {environment} from '../../environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UploadService {

  private readonly url = `${environment.apiUrl}/upload`;

  constructor(private readonly http: HttpClient) {
  }

  uploadExcel(file: File): Observable<any> {

    const formData = new FormData();
    formData.append('file', file);

    return this.http.post(`${this.url}/excel`, formData);
  }

}
