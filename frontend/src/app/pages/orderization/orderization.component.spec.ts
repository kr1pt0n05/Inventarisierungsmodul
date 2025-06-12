import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderizationComponent } from './orderization.component';

describe('OrderizationComponent', () => {
  let component: OrderizationComponent;
  let fixture: ComponentFixture<OrderizationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrderizationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrderizationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
