import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot
} from '@angular/router';
import { Observable } from 'rxjs';
import { Comment } from '../models/comment';
import { InventoriesService } from '../services/inventories.service';

@Injectable({
  providedIn: 'root'
})
export class CommentsResolver implements Resolve<Comment[]> {
  constructor(private readonly inventoriesService: InventoriesService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Comment[]> {
    return this.inventoriesService.getCommentsForId(parseInt(route.paramMap.get('id')!));
  }
}
