import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChipV2Component } from './chip-v2.component';

describe('ChipV2Component', () => {
  let component: ChipV2Component;
  let fixture: ComponentFixture<ChipV2Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChipV2Component]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChipV2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
