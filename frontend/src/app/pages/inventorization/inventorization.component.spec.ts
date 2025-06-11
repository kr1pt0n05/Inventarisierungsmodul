import { ComponentFixture, TestBed } from '@angular/core/testing';

import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentRef } from '@angular/core';
import { InventorizationComponent } from './inventorization.component';

describe('InventorizationComponent', () => {
  let component: InventorizationComponent;
  let componentRef: ComponentRef<InventorizationComponent>;
  let fixture: ComponentFixture<InventorizationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InventorizationComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(InventorizationComponent);
    component = fixture.componentInstance;
    componentRef = fixture.componentRef;
    componentRef.setInput('inventoryItem', {});
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize inventoryItemInternal with input values', () => {
    const mockItem = {
      description: 'Test',
      costCenter: '123',
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
    expect(component.inventoryItem()?.['description']).toBe('Test');
    expect(component.inventoryItem()?.['costCenter']).toBe('123');
    expect(component.inventoryItem()?.['id']).toBe(1);
  });

});
