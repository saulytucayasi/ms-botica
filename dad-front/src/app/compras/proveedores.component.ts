import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Services, Proveedor } from '../core/service/services';

@Component({
  selector: 'app-proveedores',
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './proveedores.component.html',
  styleUrl: './proveedores.component.scss'
})
export class ProveedoresComponent implements OnInit {
  proveedores: Proveedor[] = [];
  proveedorForm: FormGroup;
  loading = false;
  showForm = false;
  editingProveedor: Proveedor | null = null;
  error = '';
  success = '';
  searchTerm = '';
  showOnlyActive = false;

  constructor(
    private fb: FormBuilder,
    private services: Services
  ) {
    this.proveedorForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2)]],
      ruc: ['', [Validators.required, Validators.pattern(/^\d{11}$/)]],
      direccion: [''],
      telefono: [''],
      email: ['', [Validators.email]],
      contacto: [''],
      estado: ['ACTIVO', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.loadProveedores();
  }

  loadProveedores(): void {
    this.loading = true;
    const request = this.showOnlyActive ? 
      this.services.getProveedoresActivos() : 
      this.services.getProveedores();
    
    request.subscribe({
      next: (proveedores) => {
        this.proveedores = proveedores;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading proveedores:', error);
        this.error = 'Error al cargar los proveedores';
        this.loading = false;
      }
    });
  }

  buscarProveedores(): void {
    if (this.searchTerm.trim()) {
      this.loading = true;
      this.services.buscarProveedoresPorNombre(this.searchTerm.trim()).subscribe({
        next: (proveedores) => {
          this.proveedores = proveedores;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error searching proveedores:', error);
          this.error = 'Error al buscar proveedores';
          this.loading = false;
        }
      });
    } else {
      this.loadProveedores();
    }
  }

  toggleActiveFilter(): void {
    this.showOnlyActive = !this.showOnlyActive;
    this.loadProveedores();
  }

  openCreateForm(): void {
    this.showForm = true;
    this.editingProveedor = null;
    this.proveedorForm.reset();
    this.proveedorForm.patchValue({ estado: 'ACTIVO' });
    this.clearMessages();
  }

  openEditForm(proveedor: Proveedor): void {
    this.showForm = true;
    this.editingProveedor = proveedor;
    this.proveedorForm.patchValue({
      nombre: proveedor.nombre,
      ruc: proveedor.ruc,
      direccion: proveedor.direccion || '',
      telefono: proveedor.telefono || '',
      email: proveedor.email || '',
      contacto: proveedor.contacto || '',
      estado: proveedor.estado
    });
    this.clearMessages();
  }

  closeForm(): void {
    this.showForm = false;
    this.editingProveedor = null;
    this.proveedorForm.reset();
    this.clearMessages();
  }

  onSubmit(): void {
    if (this.proveedorForm.valid) {
      this.loading = true;
      const proveedorData: Proveedor = this.proveedorForm.value;

      if (this.editingProveedor) {
        this.services.updateProveedor(this.editingProveedor.id!, proveedorData).subscribe({
          next: (proveedor) => {
            const index = this.proveedores.findIndex(p => p.id === proveedor.id);
            if (index !== -1) {
              this.proveedores[index] = proveedor;
            }
            this.success = 'Proveedor actualizado exitosamente';
            this.closeForm();
            this.loading = false;
          },
          error: (error) => {
            console.error('Error updating proveedor:', error);
            this.error = 'Error al actualizar el proveedor';
            this.loading = false;
          }
        });
      } else {
        this.services.createProveedor(proveedorData).subscribe({
          next: (proveedor) => {
            this.proveedores.push(proveedor);
            this.success = 'Proveedor creado exitosamente';
            this.closeForm();
            this.loading = false;
          },
          error: (error) => {
            console.error('Error creating proveedor:', error);
            this.error = 'Error al crear el proveedor';
            this.loading = false;
          }
        });
      }
    }
  }

  deleteProveedor(proveedor: Proveedor): void {
    if (confirm(`¿Estás seguro de que quieres eliminar el proveedor "${proveedor.nombre}"?`)) {
      this.loading = true;
      this.services.deleteProveedor(proveedor.id!).subscribe({
        next: () => {
          this.proveedores = this.proveedores.filter(p => p.id !== proveedor.id);
          this.success = 'Proveedor eliminado exitosamente';
          this.loading = false;
        },
        error: (error) => {
          console.error('Error deleting proveedor:', error);
          this.error = 'Error al eliminar el proveedor';
          this.loading = false;
        }
      });
    }
  }

  cambiarEstado(proveedor: Proveedor): void {
    const nuevoEstado = proveedor.estado === 'ACTIVO' ? 'INACTIVO' : 'ACTIVO';
    this.loading = true;
    
    this.services.cambiarEstadoProveedor(proveedor.id!, nuevoEstado).subscribe({
      next: (proveedorActualizado) => {
        const index = this.proveedores.findIndex(p => p.id === proveedor.id);
        if (index !== -1) {
          this.proveedores[index] = proveedorActualizado;
        }
        this.success = `Proveedor ${nuevoEstado.toLowerCase()}`;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error changing status:', error);
        this.error = 'Error al cambiar el estado del proveedor';
        this.loading = false;
      }
    });
  }

  buscarPorRuc(ruc: string): void {
    if (ruc.length === 11) {
      this.loading = true;
      this.services.getProveedorByRuc(ruc).subscribe({
        next: (proveedor) => {
          this.proveedores = [proveedor];
          this.loading = false;
        },
        error: (error) => {
          console.error('Error finding by RUC:', error);
          this.error = 'Proveedor no encontrado con ese RUC';
          this.loading = false;
        }
      });
    }
  }

  clearMessages(): void {
    this.error = '';
    this.success = '';
  }

  get isEditing(): boolean {
    return this.editingProveedor !== null;
  }

  get filteredProveedores(): Proveedor[] {
    if (!this.searchTerm) {
      return this.proveedores;
    }
    
    const term = this.searchTerm.toLowerCase();
    return this.proveedores.filter(proveedor =>
      proveedor.nombre.toLowerCase().includes(term) ||
      proveedor.ruc.includes(term) ||
      (proveedor.email && proveedor.email.toLowerCase().includes(term))
    );
  }

  trackByProveedorId(index: number, proveedor: Proveedor): number {
    return proveedor.id || index;
  }

  isValidRuc(ruc: string): boolean {
    return /^\d{11}$/.test(ruc);
  }

  isValidEmail(email: string): boolean {
    if (!email) return true;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }
}