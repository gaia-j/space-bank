import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ButtonTwoComponent } from './button-two.component';

describe('ButtonTwoComponent', () => {
  let component: ButtonTwoComponent;
  let fixture: ComponentFixture<ButtonTwoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ButtonTwoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ButtonTwoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
