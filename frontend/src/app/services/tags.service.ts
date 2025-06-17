import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Tag } from '../models/tag';
import { Tags } from '../models/tags';

@Injectable({
  providedIn: 'root'
})
export class TagsService {
  private readonly url = 'http://localhost:8080/tags';

  /**
   * Constructor that injects the HttpClient service for making HTTP requests.
   *
   * @param http - Angular's HttpClient for making API requests.
   */
  constructor(private readonly http: HttpClient) { }

  /**
   * Fetches a list of tags from the backend.
   *
   * @returns {Observable<Tag[]>} - An observable containing the list of tags.
   */
  getTags(): Observable<Tags> {
    return this.http.get<Tags>(this.url);
  }

  /**
   * Adds a new tag to the backend.
   *
   * @param tag - The tag to add.
   * @returns {Observable<Tag>} - An observable containing the added tag.
   */
  addTag(tag: Tag): Observable<Tag> {
    return this.http.post<Tag>(this.url, tag);
  }
}
