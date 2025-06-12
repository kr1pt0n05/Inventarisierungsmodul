import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import {map, Observable} from 'rxjs';
import { Comment } from '../models/comment';
import { Extension } from '../models/extension';
import { Inventories } from '../models/inventories';
import { InventoryItem } from '../models/inventory-item';
import { Filter } from './server-table-data-source.service';


/**
 * InventoriesService
 *
 * This Angular service interacts with the backend API to perform CRUD operations related to inventories.
 * It makes HTTP requests to fetch inventory data, comments, extensions, changes, and other relevant metadata for inventory items.
 * The service also supports applying filters, pagination, and sorting to the inventory data.
 *
 * Features:
 * - Fetches a paginated list of inventories with sorting and filtering.
 * - Retrieves specific inventory items by their ID.
 * - Fetches related comments, extensions, and changes for inventory items.
 * - Provides methods to fetch metadata (cost centers, companies, serial numbers, etc.) for filtering purposes.
 *
 * Dependencies:
 * - `HttpClient`: Angular's HTTP client used to communicate with the backend API.
 * - `rxjs`: For working with observables and handling asynchronous data.
 * - Models for inventory data: `Inventories`, `InventoryItem`, `Comment`, `Extension`.
 */
@Injectable({
  providedIn: 'root'
})
export class InventoriesService {
  private readonly url = 'http://localhost:8080/inventories'

  /**
   * Constructor that injects the HttpClient service for making HTTP requests.
   *
   * @param http - Angular's HttpClient for making API requests.
   */
  constructor(private readonly http: HttpClient) { }

  /**
   * Fetches a paginated list of inventories with sorting and filtering options.
   *
   * @param pageNumber - The page number to fetch (for pagination).
   * @param pageSize - The number of items per page (for pagination).
   * @param sortActive - The field to sort by.
   * @param sortDirection - The direction of the sort (ascending or descending).
   * @param filter - The filter criteria to apply to the inventory data.
   * @param searchText - The text to search for in inventory descriptions.
   * @returns {Observable<Inventories>} - An observable containing the list of inventories.
   */
  getInventories(pageNumber: number, pageSize: number, sortActive: string, sortDirection: string, filter: Filter, searchText: string): Observable<Inventories> {
    const params: any = {
      'page': pageNumber,
      'size': pageSize,
      'orderBy': sortActive,
      'direction': sortDirection,
      'searchText': searchText,
    }

    // Append filter fields to params if they are defined
    Object.entries(filter).forEach(([key, value]) => {
      if (value !== undefined && value !== null) {
        // Convert arrays to comma-separated strings
        params[key] = Array.isArray(value) ? value.join(',') : value;
      }
    });

    return this.http.get<Inventories>(this.url, { params: params });
  }

  /**
   * Fetches a single inventory item by its ID.
   *
   * @param id - The ID of the inventory item to fetch.
   * @returns {Observable<InventoryItem>} - An observable containing the inventory item data.
   */
  getInventoryById(id: number): Observable<InventoryItem> {
    return this.http.get<InventoryItem>(`${this.url}/${id}`);
  }


  addInventoryItem(item: InventoryItem) {
    return this.http.post<InventoryItem>(this.url, item);
  }

  updateInventoryById(id: number, item: InventoryItem) {
    return this.http.patch<InventoryItem>(`${this.url}/${id}`, item);
  }

    /**
   * Fetches the comments associated with a specific inventory item by its ID.
   *
   * @param id - The ID of the inventory item for which to fetch comments.
   * @returns {Observable<Comment[]>} - An observable containing an array of comments.
   */
  getCommentsForId(id: number): Observable<Comment[]> {
    return this.http.get<any>(`${this.url}/${id}/comments`);
  }

  addCommentToId(id: number, comment: Comment): Observable<Comment> {
    return this.http.post<Comment>(`${this.url}/${id}/comments`, comment);
  }

  deleteCommentFromId(id: number, commentId: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}/comments/${commentId}`);
  }


   /**
   * Fetches the extensions associated with a specific inventory item by its ID.
   *
   * @param id - The ID of the inventory item for which to fetch extensions.
   * @returns {Observable<Extension[]>} - An observable containing an array of extensions.
   */
  getExtensionsForId(id: number): Observable<Extension[]> {
    return this.http.get<any>(`${this.url}/${id}/extensions`);
  }

  /**
   * Fetches the changes associated with a specific inventory item by its ID.
   *
   * @param id - The ID of the inventory item for which to fetch changes.
   * @returns {Observable<any>} - An observable containing change data (could be a custom object).
   */
  getChangesForId(id: number): Observable<any> {
    return this.http.get<any>(`${this.url}/${id}/changes`);
  }

  /**
   * Fetches all available cost centers from the backend for use in filters.
   *
   * @returns {Observable<string[]>} - An observable containing a list of cost centers.
   */
  getAllCostCenters(): Observable<string[]> {
    return this.http.get<any>(`${this.url}/costCenters`).pipe(
      map(response => response.costCenters)
    );
  }

  /**
   * Fetches all available companies from the backend for use in filters.
   *
   * @returns {Observable<string[]>} - An observable containing a list of companies.
   */
  getAllCompanies(): Observable<string[]> {
    return this.http.get<any>(`${this.url}/companies`).pipe(
      map(response => response.companies)
    );
  }

  /**
   * Fetches all available serial numbers from the backend for use in filters.
   *
   * @returns {Observable<string[]>} - An observable containing a list of serial numbers.
   */
  getAllSerialNumbers(): Observable<string[]> {
    return this.http.get<any>(`${this.url}/serialNumbers`).pipe(
      map(response => response.serialNumbers)
    );
  }

  /**
   * Fetches all available locations from the backend for use in filters.
   *
   * @returns {Observable<string[]>} - An observable containing a list of locations.
   */
  getAllLocations(): Observable<string[]> {
    return this.http.get<any>(`${this.url}/locations`).pipe(
      map(response => response.locations)
    );
  }

  /**
   * Fetches all available orderers from the backend for use in filters.
   *
   * @returns {Observable<string[]>} - An observable containing a list of orderers.
   */
  getAllOrderers(): Observable<string[]> {
    return this.http.get<any>(`${this.url}/orderers`).pipe(
      map(response => response.orderers)
    );
  }

  /**
   * Fetches all available tags from the backend for use in filters.
   *
   * @returns {Observable<string[]>} - An observable containing a list of tags.
   */
  getAlltags(): Observable<string[]> {
    return this.http.get<any>(`${this.url}/tags`).pipe(
      map(response => response.tags)
    );
  }

}
