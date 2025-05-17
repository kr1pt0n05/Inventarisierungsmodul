import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailsListComponent } from './details-list.component';

describe('detailsListComponent', () => {
  let component: DetailsListComponent;
  let fixture: ComponentFixture<DetailsListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailsListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
