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
    let extensions: Observable<Extension[]>;
    if (false) { // Set to false to use mock data

    } else {
      console.warn('Using mock data for extensions resolver');

      extensions = of([
        {
          id: 1,
          costCenter: 123,
          description: 'Produktbeschreibung',
          company: 'Firma',
          price: 99.99,
          createdAt: '2023-10-01',
          serialNumber: 'SN123456789',
          orderer: 'Besteller Name'
        },
        {
          id: 2,
          costCenter: 789,
          description: 'Weitere Produktbeschreibung',
          company: 'Andere Firma',
          price: 199.99,
          createdAt: '2023-10-02',
          serialNumber: 'SN987654321',
          orderer: 'Anderer Besteller'
        }
      ]);
    }

    return extensions;
  }
}
