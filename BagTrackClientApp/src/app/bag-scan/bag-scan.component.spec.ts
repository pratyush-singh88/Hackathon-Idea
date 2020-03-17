import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BagScanComponent } from './bag-scan.component';

describe('BagScanComponent', () => {
  let component: BagScanComponent;
  let fixture: ComponentFixture<BagScanComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BagScanComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BagScanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
