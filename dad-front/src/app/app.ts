import {Component, OnInit} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {Services} from './core/service/services';
import {Category} from './category/category';
import {CategoryModel} from './category/models/category-model';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Category],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {
  protected title = 'dad-front';
  // @ts-ignore
  public categories: CategoryModel[] = [];

  constructor(private services: Services) {
  }

  ngOnInit(): void {

    this.getCategories();
    //throw new Error('Method not implemented.');
    /*this.services.getProduct().subscribe(data => {
      console.log(data);
    });*/

  }

  private getCategories(): void {
    this.services.getCategories().subscribe(res => {
      this.categories = res;

      console.log(res);
    });
  }
}
