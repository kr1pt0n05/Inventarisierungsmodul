import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemEditorComponent } from './item-editor.component';

describe('ItemEditorComponent', () => {
  let component: ItemEditorComponent;
  let fixture: ComponentFixture<ItemEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ItemEditorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ItemEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
