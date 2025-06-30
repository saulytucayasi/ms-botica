import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { forkJoin } from 'rxjs';
import { Services, Category, Product, Inventory } from '../core/service/services';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  loading = true;
  stats = {
    totalCategories: 0,
    totalProducts: 0,
    totalInventory: 0,
    lowStockItems: 0
  };

  recentProducts: Product[] = [];
  lowStockProducts: Inventory[] = [];
  categories: Category[] = [];

  constructor(private services: Services) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.loading = true;
    
    forkJoin({
      categories: this.services.getCategories(),
      products: this.services.getProducts(),
      inventory: this.services.getInventory(),
      lowStock: this.services.getLowStockProducts()
    }).subscribe({
      next: (data) => {
        this.categories = data.categories;
        this.recentProducts = data.products.slice(0, 5); // Últimos 5 productos
        this.lowStockProducts = data.lowStock;

        this.stats = {
          totalCategories: data.categories.length,
          totalProducts: data.products.length,
          totalInventory: data.inventory.reduce((sum, item) => sum + item.stockActual, 0),
          lowStockItems: data.lowStock.length
        };

        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading dashboard data:', error);
        this.loading = false;
      }
    });
  }

  getProductsByCategory(categoryId: number): number {
    return this.recentProducts.filter(p => p.categoriaId === categoryId).length;
  }

  getCategoryName(categoryId: number): string {
    const category = this.categories.find(c => c.id === categoryId);
    return category ? category.nombre : 'Sin categoría';
  }
}
