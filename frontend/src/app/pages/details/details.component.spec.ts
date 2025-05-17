import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailsComponent } from './details.component';
import { ComponentRef } from '@angular/core';

describe('HomepageComponent', () => {
  let component: DetailsComponent;
  let componentRef: ComponentRef<DetailsComponent>;
  let fixture: ComponentFixture<DetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailsComponent);
    component = fixture.componentInstance;
    componentRef = fixture.componentRef;
    componentRef.setInput('inventoryItem', {});
    componentRef.setInput('extensions', []);
    componentRef.setInput('notes', []);
    componentRef.setInput('tags', []);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
