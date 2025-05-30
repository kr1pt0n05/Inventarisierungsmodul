import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComponentRef } from '@angular/core';
import { DetailsComponent } from './details.component';

describe('DetailsComponent', () => {
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
    componentRef.setInput('comments', []);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize inventoryItemInternal with input values', () => {
    const mockItem = {
      description: 'Test',
      costCenter: 123,
      id: 1,
      company: 'Firma',
      price: 99.99,
      createdAt: '2024-05-25',
      serialNumber: 'SN123',
      location: 'Room 1',
      orderer: 'Max',
      tags: []
    };
    componentRef.setInput('inventoryItem', mockItem);
    fixture.detectChanges();
    expect(component.inventoryItemInternal.get('description')).toBe('Test');
    expect(component.inventoryItemInternal.get('company')).toBe('Firma');
    expect(component.inventoryItemInternal.get('price')).toBe('99.99');
  });

  it('should set tags from inventoryItem input', () => {
    const mockItem = {
      description: 'Test',
      costCenter: 123,
      id: 1,
      company: 'Firma',
      price: 99.99,
      createdAt: '2024-05-25',
      serialNumber: 'SN123',
      location: 'Room 1',
      orderer: 'Max',
      tags: [{ id: 1, name: 'Tag1' }, { id: 2, name: 'Tag2' }]
    };
    componentRef.setInput('inventoryItem', mockItem);
    fixture.detectChanges();
    expect(component.tags.length).toBe(2);
    expect(component.tags.map((tag) => { return tag.name })).toEqual(['Tag1', 'Tag2']);
  });

  it('should fill inventoryItemInternal with uppercase keys if no inventoryItem is set', () => {
    componentRef.setInput('inventoryItem', undefined);
    fixture.detectChanges();
    for (let key of component.inventoryItemColumns.keys()) {
      expect(component.inventoryItemInternal.get(key)).toBe(key.toLocaleUpperCase());
    }
  });

  it('should set inventoryItemInternal to uppercase keys if inventoryItem is empty object', () => {
    componentRef.setInput('inventoryItem', {});
    fixture.detectChanges();
    for (let key of component.inventoryItemColumns.keys()) {
      expect(component.inventoryItemInternal.get(key)).toBe(key.toLocaleUpperCase());
    }
  });

  it('should set tags to empty array if inventoryItem has no tags', () => {
    const mockItem = {
      description: 'Test',
      costCenter: 123,
      id: 1,
      company: 'Firma',
      price: 99.99,
      createdAt: '2024-05-25',
      serialNumber: 'SN123',
      location: 'Room 1',
      orderer: 'Max'
      // tags is missing
    };
    componentRef.setInput('inventoryItem', mockItem);
    fixture.detectChanges();
    expect(component.tags).toEqual([]);
  });

  it('should transform changes input using mergeChangeLocation', () => {
    const changes = [
      {
        date: '2024-05-25',
        inventoryNumber: 1,
        changedBy: 'User',
        changedTable: 'inventory_items',
        changedColumn: 'location',
        oldValue: 'Alt',
        newValue: 'Neu'
      }
    ];
    componentRef.setInput('changes', changes);
    fixture.detectChanges();
    expect(component.changes().length).toBe(1);
    expect(component.changes()[0].change).toContain('Hauptartikel');
    expect(component.changes()[0].change).toContain('Standort/Nutzer:in');
  });

});
