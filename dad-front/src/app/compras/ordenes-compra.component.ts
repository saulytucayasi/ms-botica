import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormArray, FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { Services, OrdenCompra, Proveedor, Product, CrearOrdenCompraRequest } from '../core/service/services';

@Component({
  selector: 'app-ordenes-compra',
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './ordenes-compra.component.html',
  styleUrl: './ordenes-compra.component.scss'
})
export class OrdenesCompraComponent implements OnInit {
  ordenes: OrdenCompra[] = [];
  proveedores: Proveedor[] = [];
  productos: Product[] = [];
  ordenForm: FormGroup;
  loading = false;
  showForm = false;
  editingOrden: OrdenCompra | null = null;
  error = '';
  success = '';
  
  estadosOrden = ['PENDIENTE', 'ENVIADA', 'PARCIALMENTE_RECIBIDA', 'RECIBIDA', 'CANCELADA'];
  selectedEstado = '';

  constructor(
    private fb: FormBuilder,
    private services: Services
  ) {
    this.ordenForm = this.fb.group({
      proveedorId: ['', [Validators.required]],
      fechaEntregaEsperada: [''],
      observaciones: [''],
      detalles: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    forkJoin({
      ordenes: this.services.getOrdenesCompra(),
      proveedores: this.services.getProveedoresActivos(),
      productos: this.services.getProducts()
    }).subscribe({
      next: (data) => {
        this.ordenes = data.ordenes;
        this.proveedores = data.proveedores;
        this.productos = data.productos;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading data:', error);
        this.error = 'Error al cargar los datos';
        this.loading = false;
      }
    });
  }

  get detalles() {
    return this.ordenForm.get('detalles') as FormArray;
  }

  createDetalleGroup() {
    return this.fb.group({
      productoId: ['', [Validators.required]],
      cantidad: [1, [Validators.required, Validators.min(1)]],
      precioUnitario: [0, [Validators.required, Validators.min(0)]]
    });
  }

  addDetalle(): void {
    this.detalles.push(this.createDetalleGroup());
  }

  removeDetalle(index: number): void {
    this.detalles.removeAt(index);
  }

  openCreateForm(): void {
    this.showForm = true;
    this.editingOrden = null;
    this.ordenForm.reset();
    this.detalles.clear();
    this.addDetalle();
    this.clearMessages();
  }

  openEditForm(orden: OrdenCompra): void {
    this.showForm = true;
    this.editingOrden = orden;
    this.ordenForm.patchValue({
      proveedorId: orden.proveedorId,
      fechaEntregaEsperada: orden.fechaEntregaEsperada?.split('T')[0] || '',
      observaciones: orden.observaciones || ''
    });
    
    this.detalles.clear();
    if (orden.detalles) {
      orden.detalles.forEach(detalle => {
        const detalleGroup = this.fb.group({
          productoId: [detalle.productoId, [Validators.required]],
          cantidad: [detalle.cantidad, [Validators.required, Validators.min(1)]],
          precioUnitario: [detalle.precioUnitario, [Validators.required, Validators.min(0)]]
        });
        this.detalles.push(detalleGroup);
      });
    }
    this.clearMessages();
  }

  closeForm(): void {
    this.showForm = false;
    this.editingOrden = null;
    this.ordenForm.reset();
    this.detalles.clear();
    this.clearMessages();
  }

  onSubmit(): void {
    if (this.ordenForm.valid && this.detalles.length > 0) {
      this.loading = true;
      const ordenData: CrearOrdenCompraRequest = {
        ...this.ordenForm.value,
        detalles: this.detalles.value
      };

      if (this.editingOrden) {
        const updateData: OrdenCompra = {
          ...this.editingOrden,
          ...ordenData,
          detalles: ordenData.detalles.map(d => ({
            ...d,
            ordenCompraId: this.editingOrden!.id!,
            subtotal: d.cantidad * d.precioUnitario
          }))
        };
        
        this.services.updateOrdenCompra(this.editingOrden.id!, updateData).subscribe({
          next: (orden) => {
            const index = this.ordenes.findIndex(o => o.id === orden.id);
            if (index !== -1) {
              this.ordenes[index] = orden;
            }
            this.success = 'Orden de compra actualizada exitosamente';
            this.closeForm();
            this.loading = false;
          },
          error: (error) => {
            console.error('Error updating orden:', error);
            this.error = 'Error al actualizar la orden de compra';
            this.loading = false;
          }
        });
      } else {
        this.services.createOrdenCompra(ordenData).subscribe({
          next: (orden) => {
            this.ordenes.push(orden);
            this.success = 'Orden de compra creada exitosamente';
            this.closeForm();
            this.loading = false;
          },
          error: (error) => {
            console.error('Error creating orden:', error);
            this.error = 'Error al crear la orden de compra';
            this.loading = false;
          }
        });
      }
    }
  }

  deleteOrden(orden: OrdenCompra): void {
    if (confirm(`¿Estás seguro de que quieres eliminar la orden ${orden.numeroOrden}?`)) {
      this.loading = true;
      this.services.deleteOrdenCompra(orden.id!).subscribe({
        next: () => {
          this.ordenes = this.ordenes.filter(o => o.id !== orden.id);
          this.success = 'Orden de compra eliminada exitosamente';
          this.loading = false;
        },
        error: (error) => {
          console.error('Error deleting orden:', error);
          this.error = 'Error al eliminar la orden de compra';
          this.loading = false;
        }
      });
    }
  }

  cambiarEstado(orden: OrdenCompra, nuevoEstado: string): void {
    this.loading = true;
    this.services.cambiarEstadoOrdenCompra(orden.id!, nuevoEstado).subscribe({
      next: (ordenActualizada) => {
        const index = this.ordenes.findIndex(o => o.id === orden.id);
        if (index !== -1) {
          this.ordenes[index] = ordenActualizada;
        }
        this.success = `Estado cambiado a ${nuevoEstado}`;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error changing status:', error);
        this.error = 'Error al cambiar el estado';
        this.loading = false;
      }
    });
  }

  filtrarPorEstado(): void {
    if (this.selectedEstado) {
      this.loading = true;
      this.services.getOrdenesCompraPorEstado(this.selectedEstado).subscribe({
        next: (ordenes) => {
          this.ordenes = ordenes;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error filtering by status:', error);
          this.error = 'Error al filtrar por estado';
          this.loading = false;
        }
      });
    } else {
      this.loadData();
    }
  }

  getProveedorNombre(proveedorId: number): string {
    const proveedor = this.proveedores.find(p => p.id === proveedorId);
    return proveedor ? proveedor.nombre : 'Proveedor no encontrado';
  }

  getProductoNombre(productoId: number): string {
    const producto = this.productos.find(p => p.id === productoId);
    return producto ? producto.nombre : 'Producto no encontrado';
  }

  calcularSubtotal(cantidad: number, precio: number): number {
    return cantidad * precio;
  }

  calcularTotalOrden(): number {
    return this.detalles.value.reduce((total: number, detalle: any) => 
      total + (detalle.cantidad * detalle.precioUnitario), 0);
  }

  clearMessages(): void {
    this.error = '';
    this.success = '';
  }

  get isEditing(): boolean {
    return this.editingOrden !== null;
  }

  trackByOrdenId(index: number, orden: OrdenCompra): number {
    return orden.id || index;
  }
}