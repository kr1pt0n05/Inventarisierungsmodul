import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InventoryItemEditorComponent } from './inventory-item-editor.component';

describe('InventoryItemEditorComponent', () => {
  let component: InventoryItemEditorComponent;
  let fixture: ComponentFixture<InventoryItemEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InventoryItemEditorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InventoryItemEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
