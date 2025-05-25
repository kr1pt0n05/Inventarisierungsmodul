import { provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { provideHttpClient } from '@angular/common/http';
import { InventoriesService } from './inventories.service';

describe('InventoriesService', () => {
  let service: InventoriesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(InventoriesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
