import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { Services, Inventory, Product } from '../core/service/services';

@Component({
  selector: 'app-inventory',
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './inventory.component.html',
  styleUrl: './inventory.component.scss'
})
export class InventoryComponent implements OnInit {
  inventory: Inventory[] = [];
  products: Product[] = [];
  inventoryForm: FormGroup;
  loading = false;
  showForm = false;
  editingInventory: Inventory | null = null;
  error = '';
  success = '';
  selectedLocation = '';
  showLowStock = false;

  constructor(
    private fb: FormBuilder,
    private services: Services
  ) {
    this.inventoryForm = this.fb.group({
      productoId: ['', [Validators.required]],
      stockActual: [0, [Validators.required, Validators.min(0)]],
      stockMinimo: [5, [Validators.required, Validators.min(0)]],
      stockMaximo: [100, [Validators.required, Validators.min(1)]],
      costoPromedio: [0, [Validators.min(0)]],
      ubicacion: [''],
      estado: ['ACTIVO', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    forkJoin({
      inventory: this.services.getInventory(),
      products: this.services.getProducts()
    }).subscribe({
      next: (data) => {
        this.inventory = data.inventory;
        this.products = data.products;
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
    this.editingInventory = null;
    this.inventoryForm.reset();
    this.inventoryForm.patchValue({ 
      stockActual: 0, 
      stockMinimo: 5, 
      stockMaximo: 100, 
      costoPromedio: 0,
      estado: 'ACTIVO'
    });
    this.clearMessages();
  }

  openEditForm(inventory: Inventory): void {
    this.showForm = true;
    this.editingInventory = inventory;
    this.inventoryForm.patchValue({
      productoId: inventory.productoId,
      stockActual: inventory.stockActual,
      stockMinimo: inventory.stockMinimo,
      stockMaximo: inventory.stockMaximo,
      costoPromedio: inventory.costoPromedio || 0,
      ubicacion: inventory.ubicacion || '',
      estado: inventory.estado || 'ACTIVO'
    });
    this.clearMessages();
  }

  closeForm(): void {
    this.showForm = false;
    this.editingInventory = null;
    this.inventoryForm.reset();
    this.clearMessages();
  }

  onSubmit(): void {
    if (this.inventoryForm.valid) {
      this.loading = true;
      const inventoryData = this.inventoryForm.value;

      if (this.editingInventory) {
        // Update existing inventory
        this.services.updateInventory(this.editingInventory.id!, inventoryData).subscribe({
          next: (inventory) => {
            const index = this.inventory.findIndex(i => i.id === inventory.id);
            if (index !== -1) {
              this.inventory[index] = inventory;
            }
            this.success = 'Inventario actualizado exitosamente';
            this.closeForm();
            this.loading = false;
          },
          error: (error) => {
            console.error('Error updating inventory:', error);
            this.error = 'Error al actualizar el inventario';
            this.loading = false;
          }
        });
      } else {
        // Create new inventory
        this.services.createInventory(inventoryData).subscribe({
          next: (inventory) => {
            this.inventory.push(inventory);
            this.success = 'Inventario creado exitosamente';
            this.closeForm();
            this.loading = false;
          },
          error: (error) => {
            console.error('Error creating inventory:', error);
            this.error = 'Error al crear el inventario';
            this.loading = false;
          }
        });
      }
    }
  }

  deleteInventory(inventory: Inventory): void {
    if (confirm(`¿Estás seguro de que quieres eliminar el inventario del producto ID ${inventory.productoId}?`)) {
      this.loading = true;
      this.services.deleteInventory(inventory.id!).subscribe({
        next: () => {
          this.inventory = this.inventory.filter(i => i.id !== inventory.id);
          this.success = 'Inventario eliminado exitosamente';
          this.loading = false;
        },
        error: (error) => {
          console.error('Error deleting inventory:', error);
          this.error = 'Error al eliminar el inventario';
          this.loading = false;
        }
      });
    }
  }

  getProductName(productoId: number): string {
    const product = this.products.find(p => p.id === productoId);
    return product ? product.nombre : `Producto ${productoId}`;
  }

  getStockStatus(inventory: Inventory): string {
    if (inventory.stockActual <= inventory.stockMinimo) {
      return 'low';
    } else if (inventory.stockActual >= inventory.stockMaximo * 0.8) {
      return 'high';
    }
    return 'normal';
  }

  getFilteredInventory(): Inventory[] {
    let filtered = this.inventory;

    if (this.showLowStock) {
      filtered = filtered.filter(i => i.stockActual <= i.stockMinimo);
    }

    if (this.selectedLocation) {
      filtered = filtered.filter(i => i.ubicacion === this.selectedLocation);
    }

    return filtered;
  }

  getUniqueLocations(): string[] {
    const locations = this.inventory
      .map(i => i.ubicacion)
      .filter((location): location is string => location !== undefined && location !== null && location.trim() !== '')
      .filter((location, index, self) => self.indexOf(location) === index);
    return locations;
  }

  toggleLowStockFilter(): void {
    this.showLowStock = !this.showLowStock;
  }

  clearFilters(): void {
    this.showLowStock = false;
    this.selectedLocation = '';
  }

  clearMessages(): void {
    this.error = '';
    this.success = '';
  }

  get isEditing(): boolean {
    return this.editingInventory !== null;
  }

  trackByInventoryId(index: number, inventory: Inventory): number {
    return inventory.id || index;
  }
}
