import { TestBed } from '@angular/core/testing';

import { InventoriesService } from './inventories.service';

describe('InventoriesService', () => {
  let service: InventoriesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InventoriesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
