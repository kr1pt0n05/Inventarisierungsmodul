import { TestBed } from '@angular/core/testing';

import { ServerTableDataSourceService } from './server-table-data-source.service';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

describe('ServerTableDataSourceService', () => {
  let service: ServerTableDataSourceService<any>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(ServerTableDataSourceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
