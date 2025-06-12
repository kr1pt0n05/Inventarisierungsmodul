import { TestBed } from '@angular/core/testing';

import { CacheInventoryService } from './cache-inventory.service';

describe('CacheInventoryService', () => {
  let service: CacheInventoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CacheInventoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
