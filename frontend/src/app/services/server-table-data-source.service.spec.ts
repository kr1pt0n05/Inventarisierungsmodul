import { TestBed } from '@angular/core/testing';

import { ServerTableDataSourceService } from './server-table-data-source.service';

describe('ServerTableDataSourceService', () => {
  let service: ServerTableDataSourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServerTableDataSourceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
