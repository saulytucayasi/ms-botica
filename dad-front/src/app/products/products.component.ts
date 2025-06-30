import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { Services, Product, Category, Inventory } from '../core/service/services';

@Component({
  selector: 'app-products',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './products.component.html',
  styleUrl: './products.component.scss'
})
export class ProductsComponent implements OnInit {
  products: Product[] = [];
  categories: Category[] = [];
  inventory: Inventory[] = [];
  productForm: FormGroup;
  loading = false;
  showForm = false;
  editingProduct: Product | null = null;
  error = '';
  success = '';

  constructor(
    private fb: FormBuilder,
    private services: Services
  ) {
    this.productForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2)]],
      descripcion: [''],
      precio: [0, [Validators.required, Validators.min(0)]],
      categoriaId: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    forkJoin({
      products: this.services.getProducts(),
      categories: this.services.getCategories(),
      inventory: this.services.getInventory()
    }).subscribe({
      next: (data) => {
        this.products = data.products;
        this.categories = data.categories;
        this.inventory = data.inventory;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading data:', error);
        this.error = 'Error al cargar los datos';
        this.loading = false;
      }
    });
  }

  openCreateForm(): void {
    this.showForm = true;
    this.editingProduct = null;
    this.productForm.reset();
    this.productForm.patchValue({ precio: 0 });
    this.clearMessages();
  }

  openEditForm(product: Product): void {
    this.showForm = true;
    this.editingProduct = product;
    this.productForm.patchValue({
      nombre: product.nombre,
      descripcion: product.descripcion || '',
      precio: product.precio,
      categoriaId: product.categoriaId
    });
    this.clearMessages();
  }

  closeForm(): void {
    this.showForm = false;
    this.editingProduct = null;
    this.productForm.reset();
    this.clearMessages();
  }

  onSubmit(): void {
    if (this.productForm.valid) {
      this.loading = true;
      const productData = this.productForm.value;

      if (this.editingProduct) {
        // Update existing product
        this.services.updateProduct(this.editingProduct.id!, productData).subscribe({
          next: (product) => {
            const index = this.products.findIndex(p => p.id === product.id);
            if (index !== -1) {
              this.products[index] = product;
            }
            this.success = 'Producto actualizado exitosamente';
            this.closeForm();
            this.loading = false;
          },
          error: (error) => {
            console.error('Error updating product:', error);
            this.error = 'Error al actualizar el producto';
            this.loading = false;
          }
        });
      } else {
        // Create new product
        this.services.createProduct(productData).subscribe({
          next: (product) => {
            this.products.push(product);
            this.success = 'Producto creado exitosamente';
            this.closeForm();
            this.loading = false;
          },
          error: (error) => {
            console.error('Error creating product:', error);
            this.error = 'Error al crear el producto';
            this.loading = false;
          }
        });
      }
    }
  }

  deleteProduct(product: Product): void {
    if (confirm(`¿Estás seguro de que quieres eliminar el producto "${product.nombre}"?`)) {
      this.loading = true;
      this.services.deleteProduct(product.id!).subscribe({
        next: () => {
          this.products = this.products.filter(p => p.id !== product.id);
          this.success = 'Producto eliminado exitosamente';
          this.loading = false;
        },
        error: (error) => {
          console.error('Error deleting product:', error);
          this.error = 'Error al eliminar el producto';
          this.loading = false;
        }
      });
    }
  }

  getCategoryName(categoryId: number): string {
    const category = this.categories.find(c => c.id === categoryId);
    return category ? category.nombre : 'Sin categoría';
  }

  getTotalStock(productId: number): number {
    const inventoryItems = this.inventory.filter(item => item.productoId === productId);
    return inventoryItems.reduce((total, item) => total + (item.stockActual || 0), 0);
  }

  clearMessages(): void {
    this.error = '';
    this.success = '';
  }

  get isEditing(): boolean {
    return this.editingProduct !== null;
  }

  trackByProductId(index: number, product: Product): number {
    return product.id || index;
  }
}
