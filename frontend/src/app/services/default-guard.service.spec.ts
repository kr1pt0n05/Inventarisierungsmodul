import { TestBed } from '@angular/core/testing';

import { DefaultGuardService } from './default-guard.service';

describe('DefaultGuardService', () => {
  let service: DefaultGuardService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DefaultGuardService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
