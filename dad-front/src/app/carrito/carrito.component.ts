import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Services, Carrito, Product, AgregarCarritoRequest, Inventory } from '../core/service/services';
import { IconComponent } from '../shared/components/icons/icon.component';

@Component({
  selector: 'app-carrito',
  standalone: true,
  imports: [CommonModule, FormsModule, IconComponent],
  templateUrl: './carrito.component.html',
  styleUrls: ['./carrito.component.scss']
})
export class CarritoComponent implements OnInit {
  carrito: Carrito | null = null;
  products: Product[] = [];
  availableProducts: Product[] = [];
  inventories: Inventory[] = [];
  selectedProductId: number = 0;
  cantidad: number = 1;
  clienteId: number = 1; // Por ahora hardcodeado
  loading: boolean = false;
  error: string | null = null;
  deletingItems: Set<number> = new Set(); // Track which items are being deleted

  constructor(private services: Services, private router: Router) {}

  ngOnInit(): void {
    this.loadProducts();
    this.loadCarrito();
  }

  loadProducts(): void {
    this.loading = true;
    // Cargar productos e inventarios en paralelo
    Promise.all([
      this.services.getProducts().toPromise(),
      this.services.getInventory().toPromise()
    ]).then(([products, inventories]) => {
      this.products = products || [];
      this.inventories = inventories || [];
      this.filterAvailableProducts();
      this.loading = false;
    }).catch((error) => {
      console.error('Error loading products and inventory:', error);
      this.error = 'Error al cargar productos e inventario';
      this.loading = false;
    });
  }

  filterAvailableProducts(): void {
    this.availableProducts = this.products.filter(product => {
      const inventory = this.inventories.find(inv => inv.productoId === product.id);
      return inventory && inventory.stockActual > 0;
    });
  }

  loadCarrito(): void {
    this.loading = true;
    this.services.getCarritoByCliente(this.clienteId).subscribe({
      next: (carrito) => {
        this.carrito = carrito;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading carrito:', error);
        this.error = 'Error al cargar carrito';
        this.loading = false;
      }
    });
  }

  agregarProducto(): void {
    if (this.selectedProductId === 0 || this.cantidad <= 0) {
      this.error = 'Seleccione un producto y cantidad válida';
      return;
    }

    const request: AgregarCarritoRequest = {
      productoId: this.selectedProductId,
      cantidad: this.cantidad
    };

    this.loading = true;
    this.services.agregarProductoAlCarrito(this.clienteId, request).subscribe({
      next: (carrito) => {
        this.carrito = carrito;
        this.selectedProductId = 0;
        this.cantidad = 1;
        this.error = null;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error adding product:', error);
        this.error = 'Error al agregar producto al carrito';
        this.loading = false;
      }
    });
  }

  actualizarCantidad(productoId: number, nuevaCantidad: number): void {
    if (nuevaCantidad <= 0) {
      this.eliminarItem(productoId);
      return;
    }

    this.loading = true;
    this.services.actualizarCantidadItem(this.clienteId, productoId, nuevaCantidad).subscribe({
      next: (carrito) => {
        this.carrito = carrito;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error updating quantity:', error);
        this.error = 'Error al actualizar cantidad';
        this.loading = false;
      }
    });
  }

  eliminarItem(productoId: number): void {
    // Prevent multiple clicks for the same item
    if (this.deletingItems.has(productoId)) return;
    
    this.deletingItems.add(productoId);
    this.services.eliminarProductoDelCarrito(this.clienteId, productoId).subscribe({
      next: (carrito) => {
        this.carrito = carrito;
        this.error = null;
        this.deletingItems.delete(productoId);
      },
      error: (error) => {
        console.error('Error removing item:', error);
        this.error = 'Error al eliminar producto del carrito';
        this.deletingItems.delete(productoId);
      }
    });
  }

  isDeleting(productoId: number): boolean {
    return this.deletingItems.has(productoId);
  }

  limpiarCarrito(): void {
    if (confirm('¿Está seguro de que desea limpiar el carrito?')) {
      this.loading = true;
      this.services.limpiarCarrito(this.clienteId).subscribe({
        next: () => {
          // Reset carrito to empty state
          if (this.carrito) {
            this.carrito.items = [];
            this.carrito.total = 0;
          }
          this.error = null;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error clearing cart:', error);
          this.error = 'Error al limpiar carrito';
          this.loading = false;
        }
      });
    }
  }

  getProductName(productoId: number): string {
    const product = this.products.find(p => p.id === productoId);
    return product ? product.nombre : 'Producto no encontrado';
  }

  getTotal(): number {
    return this.carrito?.total || 0;
  }

  procederAPagar(): void {
    this.router.navigate(['/ventas']);
  }

  getAvailableStock(productoId: number): number {
    const inventory = this.inventories.find(inv => inv.productoId === productoId);
    return inventory ? inventory.stockActual : 0;
  }
}