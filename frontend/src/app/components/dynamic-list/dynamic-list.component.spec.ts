import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DynamicListComponent } from './dynamic-list.component';
import { ComponentRef } from '@angular/core';

describe('dynamicListComponent', () => {
  let component: DynamicListComponent;
  let componentRef: ComponentRef<DynamicListComponent>;
  let fixture: ComponentFixture<DynamicListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DynamicListComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(DynamicListComponent);
    component = fixture.componentInstance;
    componentRef = fixture.componentRef;
    componentRef.setInput('tableContent', []);
    componentRef.setInput('columns', new Map([]));
    fixture.detectChanges();
  });


  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize data source with tableContent', () => {
    componentRef.setInput('tableContent', [{ name: 'Max' }, { name: 'Anna' }]);
    componentRef.setInput('columns', new Map([['name', 'Name']]));
    fixture.detectChanges();
    expect(component.items.data.length).toBe(2);
  });

  it('should return correct column ids', () => {
    componentRef.setInput('columns', new Map([['name', 'Name'], ['age', 'Age']]));
    fixture.detectChanges();
    expect(component.getColumnIds()).toEqual(['name', 'age']);
  });

});
