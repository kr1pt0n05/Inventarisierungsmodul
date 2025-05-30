import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot
} from '@angular/router';
import { Observable, of } from 'rxjs';
import { Extension } from '../models/extension';

@Injectable({
  providedIn: 'root'
})
export class ExtensionsResolver implements Resolve<Extension[]> {
  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Extension[]> {
    // Mock data for demonstration purposes
    const extensions: Extension[] = [
      {
        costCenter: 123,
        inventoryNumber: parseInt(route.paramMap.get('id')!),
        productDescription: 'Produktbeschreibung',
        company: 'Firma',
        price: 99.99,
        date: '2023-10-01',
        serialNumber: 'SN123456789',
        orderer: 'Besteller Name'
      },
      {
        costCenter: 789,
        inventoryNumber: parseInt(route.paramMap.get('id')!),
        productDescription: 'Weitere Produktbeschreibung',
        company: 'Andere Firma',
        price: 199.99,
        date: '2023-10-02',
        serialNumber: 'SN987654321',
        orderer: 'Anderer Besteller'
      }
    ];
    console.log('ExtensionsResolver: Resolving extensions', extensions);
    return of(extensions);
  }
}
