import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot
} from '@angular/router';
import { Observable, of } from 'rxjs';
import { InventoryItemNotes } from '../models/inventory-item-notes';

@Injectable({
  providedIn: 'root'
})
export class InventoryItemNotesResolver implements Resolve<InventoryItemNotes[]> {
  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<InventoryItemNotes[]> {
    const notes: InventoryItemNotes[] = [
      {
        note: 'Dies ist eine Beispielnotiz für das Inventaritem.',
        date: '2023-10-01',
        author: 'Max Mustermann'
      },
      {
        note: 'Eine weitere Notiz, die dem Inventaritem hinzugefügt wurde.',
        date: '2023-10-02',
        author: 'Erika Musterfrau'
      }
    ];
    return of(notes);
  }
}
