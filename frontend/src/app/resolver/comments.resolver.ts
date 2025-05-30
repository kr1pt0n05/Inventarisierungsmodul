import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot
} from '@angular/router';
import { Observable, of } from 'rxjs';
import { Comment } from '../models/comment';

@Injectable({
  providedIn: 'root'
})
export class CommentsResolver implements Resolve<Comment[]> {
  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Comment[]> {
    const comments: Comment[] = [
      {
        comment: 'Dies ist eine Beispielnotiz für das Inventaritem.',
        createdAt: '2023-10-01',
        author: 'Max Mustermann'
      },
      {
        comment: 'Eine weitere Notiz, die dem Inventaritem hinzugefügt wurde.',
        createdAt: '2023-10-02',
        author: 'Erika Musterfrau'
      }
    ];
    return of(comments);
  }
}
