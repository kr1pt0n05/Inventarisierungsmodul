import { TestBed } from '@angular/core/testing';

import { InventoryItemNotesResolver } from './notes.resolver';

describe('InventoryItemNotesResolver', () => {
  let resolver: InventoryItemNotesResolver;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    resolver = TestBed.inject(InventoryItemNotesResolver);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });
});
