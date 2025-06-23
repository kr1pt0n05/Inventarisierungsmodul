import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environment';

@Injectable({
  providedIn: 'root'
})
export class DownloadService {

  private readonly url = `${environment.apiUrl}/download`;

  constructor(private readonly http: HttpClient) {
  }

  downloadExcel(): Observable<any> {
    return this.http.get(`${this.url}/xls`, { responseType: 'blob' });
  }
}
