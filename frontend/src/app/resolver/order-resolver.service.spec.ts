import { TestBed } from '@angular/core/testing';

import { OrderResolverService } from './order-resolver.service';

describe('OrderResolverService', () => {
  let service: OrderResolverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrderResolverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
