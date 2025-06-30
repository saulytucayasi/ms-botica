import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Services, Category as CategoryInterface } from '../core/service/services';

@Component({
  selector: 'app-category',
  imports: [CommonModule, ReactiveFormsModule],
  standalone: true,
  templateUrl: './category.html',
  styleUrl: './category.scss'
})
export class CategoryComponent implements OnInit {
  categories: CategoryInterface[] = [];
  categoryForm: FormGroup;
  loading = false;
  showForm = false;
  editingCategory: CategoryInterface | null = null;
  error = '';
  success = '';

  constructor(
    private fb: FormBuilder,
    private services: Services
  ) {
    this.categoryForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2)]],
      descripcion: ['']
    });
  }

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.loading = true;
    this.services.getCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading categories:', error);
        this.error = 'Error al cargar las categorías';
        this.loading = false;
      }
    });
  }

  openCreateForm(): void {
    this.showForm = true;
    this.editingCategory = null;
    this.categoryForm.reset();
    this.clearMessages();
  }

  openEditForm(category: CategoryInterface): void {
    this.showForm = true;
    this.editingCategory = category;
    this.categoryForm.patchValue({
      nombre: category.nombre,
      descripcion: category.descripcion || ''
    });
    this.clearMessages();
  }

  closeForm(): void {
    this.showForm = false;
    this.editingCategory = null;
    this.categoryForm.reset();
    this.clearMessages();
  }

  onSubmit(): void {
    if (this.categoryForm.valid) {
      this.loading = true;
      const categoryData = this.categoryForm.value;

      if (this.editingCategory) {
        // Update existing category
        this.services.updateCategory(this.editingCategory.id!, categoryData).subscribe({
          next: (category) => {
            const index = this.categories.findIndex(c => c.id === category.id);
            if (index !== -1) {
              this.categories[index] = category;
            }
            this.success = 'Categoría actualizada exitosamente';
            this.closeForm();
            this.loading = false;
          },
          error: (error) => {
            console.error('Error updating category:', error);
            this.error = 'Error al actualizar la categoría';
            this.loading = false;
          }
        });
      } else {
        // Create new category
        this.services.createCategory(categoryData).subscribe({
          next: (category) => {
            this.categories.push(category);
            this.success = 'Categoría creada exitosamente';
            this.closeForm();
            this.loading = false;
          },
          error: (error) => {
            console.error('Error creating category:', error);
            this.error = 'Error al crear la categoría';
            this.loading = false;
          }
        });
      }
    }
  }

  deleteCategory(category: CategoryInterface): void {
    if (confirm(`¿Estás seguro de que quieres eliminar la categoría "${category.nombre}"?`)) {
      this.loading = true;
      this.services.deleteCategory(category.id!).subscribe({
        next: () => {
          this.categories = this.categories.filter(c => c.id !== category.id);
          this.success = 'Categoría eliminada exitosamente';
          this.loading = false;
        },
        error: (error) => {
          console.error('Error deleting category:', error);
          this.error = 'Error al eliminar la categoría';
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
    return this.editingCategory !== null;
  }
}
