import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExtensionInventorizationComponent } from './extension-inventorization.component';

describe('ExtensionInventorizationComponent', () => {
  let component: ExtensionInventorizationComponent;
  let fixture: ComponentFixture<ExtensionInventorizationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExtensionInventorizationComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ExtensionInventorizationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
