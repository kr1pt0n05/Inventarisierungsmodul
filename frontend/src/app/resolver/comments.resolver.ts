import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  Router,
  RouterStateSnapshot
} from '@angular/router';
import { catchError, map, Observable, of } from 'rxjs';
import { Comment } from '../models/comment';
import { InventoriesService } from '../services/inventories.service';

@Injectable({
  providedIn: 'root'
})
export class CommentsResolver implements Resolve<Comment[]> {
  constructor(private readonly inventoriesService: InventoriesService,
    private readonly router: Router) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Comment[]> {
    return this.inventoriesService.getCommentsForId(parseInt(route.paramMap.get('id')!)).pipe(
      map(comments => comments),
      catchError(error => {
        return of([]) as Observable<Comment[]>;
      })
    );
  }
}
