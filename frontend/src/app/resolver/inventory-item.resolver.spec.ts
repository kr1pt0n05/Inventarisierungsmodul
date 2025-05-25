import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { InventoriesService } from '../services/inventories.service';
import { InventoryItemResolver } from './inventory-item.resolver';

describe('InventoryItemResolver', () => {
  let resolver: InventoryItemResolver;
  let inventoriesServiceSpy: jasmine.SpyObj<InventoriesService>;

  beforeEach(() => {
    const spy = jasmine.createSpyObj('InventoriesService', ['getInventoryById']);
    TestBed.configureTestingModule({
      providers: [
        InventoryItemResolver,
        { provide: InventoriesService, useValue: spy }
      ]
    });
    resolver = TestBed.inject(InventoryItemResolver);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });

  it('should return mock data when debug is true', (done) => {
    const route = {
      paramMap: {
        get: (key: string) => '42'
      }
    } as unknown as ActivatedRouteSnapshot;
    const state = {} as RouterStateSnapshot;

    // debug is hardcoded to true in resolver
    const result$ = resolver.resolve(route, state);
    result$.subscribe(result => {
      expect(result).toBeTruthy();
      expect(result.id).toBe(42);
      expect(result.company).toBe('firma');
      done();
    });
  });

  it('should handle missing id parameter gracefully', (done) => {
    const route = {
      paramMap: {
        get: (key: string) => null
      }
    } as unknown as ActivatedRouteSnapshot;
    const state = {} as RouterStateSnapshot;

    const result$ = resolver.resolve(route, state);
    result$.subscribe(result => {
      expect(result).toBeTruthy();
      expect(result.id).toBeNaN();
      done();
    });
  });
});
