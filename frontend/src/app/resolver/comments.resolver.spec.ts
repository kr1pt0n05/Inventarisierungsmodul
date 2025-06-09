import { TestBed } from '@angular/core/testing';

import { CommentsResolver } from './comments.resolver';

describe('CommentsResolver', () => {
  let resolver: CommentsResolver;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    resolver = TestBed.inject(CommentsResolver);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });
});
