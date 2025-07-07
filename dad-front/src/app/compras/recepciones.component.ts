import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormArray, FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { Services, RecepcionMercancia, OrdenCompra, Product, CrearRecepcionRequest } from '../core/service/services';

@Component({
  selector: 'app-recepciones',
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './recepciones.component.html',
  styleUrl: './recepciones.component.scss'
})
export class RecepcionesComponent implements OnInit {
  recepciones: RecepcionMercancia[] = [];
  ordenesCompra: OrdenCompra[] = [];
  productos: Product[] = [];
  recepcionForm: FormGroup;
  loading = false;
  showForm = false;
  editingRecepcion: RecepcionMercancia | null = null;
  error = '';
  success = '';
  
  estadosRecepcion = ['PENDIENTE', 'PARCIAL', 'COMPLETA', 'CANCELADA'];
  selectedEstado = '';
  selectedOrdenId = '';

  constructor(
    private fb: FormBuilder,
    private services: Services
  ) {
    this.recepcionForm = this.fb.group({
      ordenCompraId: ['', [Validators.required]],
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
      recepciones: this.services.getRecepciones(),
      ordenes: this.services.getOrdenesCompra(),
      productos: this.services.getProducts()
    }).subscribe({
      next: (data) => {
        this.recepciones = data.recepciones;
        this.ordenesCompra = data.ordenes.filter(orden => 
          orden.estado === 'ENVIADA' || orden.estado === 'PARCIALMENTE_RECIBIDA'
        );
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
    return this.recepcionForm.get('detalles') as FormArray;
  }

  createDetalleGroup() {
    return this.fb.group({
      productoId: ['', [Validators.required]],
      cantidadEsperada: [0, [Validators.required, Validators.min(0)]],
      cantidadRecibida: [0, [Validators.required, Validators.min(0)]],
      precioUnitario: [0, [Validators.required, Validators.min(0)]]
    });
  }

  addDetalle(): void {
    this.detalles.push(this.createDetalleGroup());
  }

  removeDetalle(index: number): void {
    this.detalles.removeAt(index);
  }

  onOrdenChange(): void {
    const ordenId = this.recepcionForm.get('ordenCompraId')?.value;
    if (ordenId) {
      const orden = this.ordenesCompra.find(o => o.id == ordenId);
      if (orden && orden.detalles) {
        this.detalles.clear();
        orden.detalles.forEach(detalle => {
          const detalleGroup = this.fb.group({
            productoId: [detalle.productoId, [Validators.required]],
            cantidadEsperada: [detalle.cantidad, [Validators.required, Validators.min(0)]],
            cantidadRecibida: [0, [Validators.required, Validators.min(0)]],
            precioUnitario: [detalle.precioUnitario, [Validators.required, Validators.min(0)]]
          });
          this.detalles.push(detalleGroup);
        });
      }
    }
  }

  openCreateForm(): void {
    this.showForm = true;
    this.editingRecepcion = null;
    this.recepcionForm.reset();
    this.detalles.clear();
    this.clearMessages();
  }

  openEditForm(recepcion: RecepcionMercancia): void {
    this.showForm = true;
    this.editingRecepcion = recepcion;
    this.recepcionForm.patchValue({
      ordenCompraId: recepcion.ordenCompraId,
      observaciones: recepcion.observaciones || ''
    });
    
    this.detalles.clear();
    if (recepcion.detalles) {
      recepcion.detalles.forEach(detalle => {
        const detalleGroup = this.fb.group({
          productoId: [detalle.productoId, [Validators.required]],
          cantidadEsperada: [detalle.cantidadEsperada, [Validators.required, Validators.min(0)]],
          cantidadRecibida: [detalle.cantidadRecibida, [Validators.required, Validators.min(0)]],
          precioUnitario: [detalle.precioUnitario, [Validators.required, Validators.min(0)]]
        });
        this.detalles.push(detalleGroup);
      });
    }
    this.clearMessages();
  }

  closeForm(): void {
    this.showForm = false;
    this.editingRecepcion = null;
    this.recepcionForm.reset();
    this.detalles.clear();
    this.clearMessages();
  }

  onSubmit(): void {
    if (this.recepcionForm.valid && this.detalles.length > 0) {
      this.loading = true;
      const recepcionData: CrearRecepcionRequest = {
        ...this.recepcionForm.value,
        detalles: this.detalles.value
      };

      if (this.editingRecepcion) {
        const updateData: RecepcionMercancia = {
          ...this.editingRecepcion,
          ...recepcionData,
          detalles: recepcionData.detalles.map(d => ({
            ...d,
            recepcionId: this.editingRecepcion!.id!,
            subtotal: d.cantidadRecibida * d.precioUnitario
          }))
        };
        
        this.services.updateRecepcion(this.editingRecepcion.id!, updateData).subscribe({
          next: (recepcion) => {
            const index = this.recepciones.findIndex(r => r.id === recepcion.id);
            if (index !== -1) {
              this.recepciones[index] = recepcion;
            }
            this.success = 'Recepción actualizada exitosamente';
            this.closeForm();
            this.loading = false;
          },
          error: (error) => {
            console.error('Error updating recepcion:', error);
            this.error = 'Error al actualizar la recepción';
            this.loading = false;
          }
        });
      } else {
        this.services.createRecepcion(recepcionData).subscribe({
          next: (recepcion) => {
            this.recepciones.push(recepcion);
            this.success = 'Recepción creada exitosamente';
            this.closeForm();
            this.loading = false;
          },
          error: (error) => {
            console.error('Error creating recepcion:', error);
            this.error = 'Error al crear la recepción';
            this.loading = false;
          }
        });
      }
    }
  }

  deleteRecepcion(recepcion: RecepcionMercancia): void {
    if (confirm(`¿Estás seguro de que quieres eliminar la recepción ${recepcion.numeroRecepcion}?`)) {
      this.loading = true;
      this.services.deleteRecepcion(recepcion.id!).subscribe({
        next: () => {
          this.recepciones = this.recepciones.filter(r => r.id !== recepcion.id);
          this.success = 'Recepción eliminada exitosamente';
          this.loading = false;
        },
        error: (error) => {
          console.error('Error deleting recepcion:', error);
          this.error = 'Error al eliminar la recepción';
          this.loading = false;
        }
      });
    }
  }

  cambiarEstado(recepcion: RecepcionMercancia, nuevoEstado: string): void {
    this.loading = true;
    this.services.cambiarEstadoRecepcion(recepcion.id!, nuevoEstado).subscribe({
      next: (recepcionActualizada) => {
        const index = this.recepciones.findIndex(r => r.id === recepcion.id);
        if (index !== -1) {
          this.recepciones[index] = recepcionActualizada;
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

  procesarRecepcion(recepcion: RecepcionMercancia): void {
    if (confirm(`¿Procesar la recepción ${recepcion.numeroRecepcion}? Esto actualizará el inventario.`)) {
      this.loading = true;
      this.services.procesarRecepcion(recepcion.id!).subscribe({
        next: (recepcionProcesada) => {
          const index = this.recepciones.findIndex(r => r.id === recepcion.id);
          if (index !== -1) {
            this.recepciones[index] = recepcionProcesada;
          }
          this.success = 'Recepción procesada exitosamente';
          this.loading = false;
        },
        error: (error) => {
          console.error('Error processing recepcion:', error);
          this.error = 'Error al procesar la recepción';
          this.loading = false;
        }
      });
    }
  }

  filtrarPorEstado(): void {
    if (this.selectedEstado) {
      this.loading = true;
      this.services.getRecepcionesPorEstado(this.selectedEstado).subscribe({
        next: (recepciones) => {
          this.recepciones = recepciones;
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

  filtrarPorOrden(): void {
    if (this.selectedOrdenId) {
      this.loading = true;
      this.services.getRecepcionesPorOrdenCompra(Number(this.selectedOrdenId)).subscribe({
        next: (recepciones) => {
          this.recepciones = recepciones;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error filtering by orden:', error);
          this.error = 'Error al filtrar por orden';
          this.loading = false;
        }
      });
    } else {
      this.loadData();
    }
  }

  getOrdenNumero(ordenId: number): string {
    const orden = this.ordenesCompra.find(o => o.id === ordenId);
    return orden ? orden.numeroOrden : 'Orden no encontrada';
  }

  getProductoNombre(productoId: number): string {
    const producto = this.productos.find(p => p.id === productoId);
    return producto ? producto.nombre : 'Producto no encontrado';
  }

  calcularSubtotal(cantidadRecibida: number, precio: number): number {
    return cantidadRecibida * precio;
  }

  calcularPorcentajeRecibido(cantidadRecibida: number, cantidadEsperada: number): number {
    return cantidadEsperada > 0 ? (cantidadRecibida / cantidadEsperada) * 100 : 0;
  }

  getTotalEsperado(): number {
    return this.detalles.controls.reduce((sum, control) => 
      sum + (control.get('cantidadEsperada')?.value || 0), 0);
  }

  getTotalRecibido(): number {
    return this.detalles.controls.reduce((sum, control) => 
      sum + (control.get('cantidadRecibida')?.value || 0), 0);
  }

  getProgresoGlobal(): number {
    const totalEsperado = this.getTotalEsperado();
    const totalRecibido = this.getTotalRecibido();
    return this.calcularPorcentajeRecibido(totalRecibido, totalEsperado);
  }

  getProgresoRecepcion(recepcion: RecepcionMercancia): number {
    if (!recepcion.detalles || recepcion.detalles.length === 0) {
      return 0;
    }
    const totalRecibido = recepcion.detalles.reduce((sum, d) => sum + d.cantidadRecibida, 0);
    const totalEsperado = recepcion.detalles.reduce((sum, d) => sum + d.cantidadEsperada, 0);
    return this.calcularPorcentajeRecibido(totalRecibido, totalEsperado);
  }

  clearMessages(): void {
    this.error = '';
    this.success = '';
  }

  get isEditing(): boolean {
    return this.editingRecepcion !== null;
  }

  trackByRecepcionId(index: number, recepcion: RecepcionMercancia): number {
    return recepcion.id || index;
  }
}