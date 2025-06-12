import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommentsEditorComponent } from './comments-editor.component';

describe('CommentsEditorComponent', () => {
  let component: CommentsEditorComponent;
  let fixture: ComponentFixture<CommentsEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CommentsEditorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CommentsEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
