import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoadUnloadComponent } from './load-unload.component';

describe('LoadUnloadComponent', () => {
  let component: LoadUnloadComponent;
  let fixture: ComponentFixture<LoadUnloadComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LoadUnloadComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoadUnloadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
