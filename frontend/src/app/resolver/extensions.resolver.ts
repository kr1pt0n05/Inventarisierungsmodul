import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot
} from '@angular/router';
import { Observable, of } from 'rxjs';
import { Extension } from '../models/extension';
import { InventoriesService } from '../services/inventories.service';

@Injectable({
  providedIn: 'root'
})
export class ExtensionsResolver implements Resolve<Extension[]> {
  constructor(private readonly inventoriesService: InventoriesService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Extension[]> {
    const debug = false
    if (!debug) { // Set to false to use mock data
      return this.inventoriesService.getExtensionsForId(parseInt(route.paramMap.get('id')!));

    } else {
      console.warn('Using mock data for extensions resolver');

      return of([
        {
          id: 1,
          cost_center: '123',
          description: 'Produktbeschreibung',
          company: 'Firma',
          price: 99.99,
          created_at: '2023-10-01',
          serial_number: 'SN123456789',
          orderer: 'Besteller Name'
        },
        {
          id: 2,
          cost_center: '789',
          description: 'Weitere Produktbeschreibung',
          company: 'Andere Firma',
          price: 199.99,
          created_at: '2023-10-02',
          serial_number: 'SN987654321',
          orderer: 'Anderer Besteller'
        }
      ]);
    }
  }
}
