import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Category } from './category';

describe('CategoryModel', () => {
  let component: Category;
  let fixture: ComponentFixture<Category>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Category]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Category);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
