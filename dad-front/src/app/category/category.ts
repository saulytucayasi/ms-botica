import {Component, Input, OnInit} from '@angular/core';
import {CategoryModel} from './models/category-model';
import {CommonModule, NgFor} from '@angular/common';

@Component({
  selector: 'app-category',
  imports: [CommonModule, NgFor],
  standalone: true,
  templateUrl: './category.html',
  styleUrl: './category.scss'
})
export class Category implements OnInit {

  @Input() categories: CategoryModel[] = [];

  ngOnInit() {
    console.log("=======>>",this.categories);
  }
}
