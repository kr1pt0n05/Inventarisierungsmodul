import { TestBed } from '@angular/core/testing';

import { ExtensionsResolver } from './extensions.resolver';

describe('ExtensionsResolver', () => {
  let resolver: ExtensionsResolver;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    resolver = TestBed.inject(ExtensionsResolver);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });
});
