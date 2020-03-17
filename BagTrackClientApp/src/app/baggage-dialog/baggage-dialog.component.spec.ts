import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BaggageDialogComponent } from './baggage-dialog.component';

describe('BaggageDialogComponent', () => {
  let component: BaggageDialogComponent;
  let fixture: ComponentFixture<BaggageDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BaggageDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BaggageDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
