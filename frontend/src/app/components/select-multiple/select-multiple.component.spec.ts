import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectMultipleComponent } from './select-multiple.component';

describe('SelectMultipleComponent', () => {
  let component: SelectMultipleComponent;
  let fixture: ComponentFixture<SelectMultipleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SelectMultipleComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SelectMultipleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
