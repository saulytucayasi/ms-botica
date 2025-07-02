import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Services, Venta } from '../core/service/services';

@Component({
  selector: 'app-recibo',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './recibo.component.html',
  styleUrls: ['./recibo.component.scss']
})
export class ReciboComponent implements OnInit {
  venta: Venta | null = null;
  loading: boolean = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private services: Services
  ) {}

  ngOnInit(): void {
    const ventaId = this.route.snapshot.paramMap.get('id');
    if (ventaId) {
      this.loadVenta(+ventaId);
    } else {
      this.error = 'ID de venta no válido';
      this.loading = false;
    }
  }

  loadVenta(ventaId: number): void {
    this.loading = true;
    this.services.getVentaById(ventaId).subscribe({
      next: (venta) => {
        this.venta = venta;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading venta:', error);
        this.error = 'Error al cargar los detalles de la venta';
        this.loading = false;
      }
    });
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  imprimirRecibo(): void {
    window.print();
  }

  volverAVentas(): void {
    this.router.navigate(['/ventas']);
  }

  nuevaVenta(): void {
    this.router.navigate(['/carrito']);
  }
}