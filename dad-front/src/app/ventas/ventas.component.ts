import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Services, Venta, Carrito, CrearVentaRequest } from '../core/service/services';

@Component({
  selector: 'app-ventas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './ventas.component.html',
  styleUrls: ['./ventas.component.scss']
})
export class VentasComponent implements OnInit {
  ventas: Venta[] = [];
  carrito: Carrito | null = null;
  selectedVenta: Venta | null = null;
  clienteId: number = 1; // Por ahora hardcodeado
  loading: boolean = false;
  error: string | null = null;
  showCheckout: boolean = false;

  constructor(
    private services: Services,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadVentas();
    this.loadCarrito();
  }

  loadVentas(): void {
    this.loading = true;
    this.services.getVentasByCliente(this.clienteId).subscribe({
      next: (ventas) => {
        this.ventas = ventas;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading ventas:', error);
        this.error = 'Error al cargar ventas';
        this.loading = false;
      }
    });
  }

  loadCarrito(): void {
    this.services.getCarritoByCliente(this.clienteId).subscribe({
      next: (carrito) => {
        this.carrito = carrito;
      },
      error: (error) => {
        console.error('Error loading carrito:', error);
      }
    });
  }

  loadVentaDetails(ventaId: number): void {
    this.loading = true;
    this.services.getVentaById(ventaId).subscribe({
      next: (venta) => {
        this.selectedVenta = venta;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading venta details:', error);
        this.error = 'Error al cargar detalles de la venta';
        this.loading = false;
      }
    });
  }

  realizarVenta(): void {
    if (!this.carrito || !this.carrito.items || this.carrito.items.length === 0) {
      this.error = 'No hay productos en el carrito';
      return;
    }

    const request: CrearVentaRequest = {
      clienteId: this.clienteId,
      carritoId: this.carrito.id!,
      observaciones: 'Venta realizada desde la aplicación web'
    };

    this.loading = true;
    this.services.realizarVenta(request).subscribe({
      next: (venta) => {
        this.error = null;
        this.showCheckout = false;
        this.loadVentas();
        this.loadCarrito();
        this.loading = false;
        // Navegar directamente al recibo
        this.router.navigate(['/recibo', venta.id]);
      },
      error: (error) => {
        console.error('Error realizando venta:', error);
        this.error = 'Error al realizar la venta: ' + (error.error?.message || error.message);
        this.loading = false;
      }
    });
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  getEstadoClass(estado: string): string {
    switch (estado) {
      case 'CONFIRMADA':
        return 'badge-success';
      case 'PENDIENTE':
        return 'badge-warning';
      case 'CANCELADA':
        return 'badge-danger';
      default:
        return 'badge-secondary';
    }
  }

  toggleCheckout(): void {
    this.showCheckout = !this.showCheckout;
    if (this.showCheckout) {
      this.loadCarrito();
    }
  }

  goToCarrito(): void {
    this.router.navigate(['/carrito']);
  }

  closeVentaDetails(): void {
    this.selectedVenta = null;
  }
}