import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FlightDelayComponent } from './flight-delay.component';

describe('FlightDelayComponent', () => {
  let component: FlightDelayComponent;
  let fixture: ComponentFixture<FlightDelayComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FlightDelayComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FlightDelayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
