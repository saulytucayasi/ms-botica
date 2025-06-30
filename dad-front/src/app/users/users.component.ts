import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../core/service/auth.service';
import { AuthUser } from '../core/service/services';

@Component({
  selector: 'app-users',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './users.component.html',
  styleUrl: './users.component.scss'
})
export class UsersComponent implements OnInit {
  users: AuthUser[] = [];
  userForm: FormGroup;
  loading = false;
  showForm = false;
  editingUser: AuthUser | null = null;
  error = '';
  success = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService
  ) {
    this.userForm = this.fb.group({
      userName: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    // Note: This would need to be implemented in the backend
    // For now, we'll just show the registration form
    this.users = [];
  }

  openCreateForm(): void {
    this.showForm = true;
    this.editingUser = null;
    this.userForm.reset();
    this.clearMessages();
  }

  closeForm(): void {
    this.showForm = false;
    this.editingUser = null;
    this.userForm.reset();
    this.clearMessages();
  }

  onSubmit(): void {
    if (this.userForm.valid) {
      this.loading = true;
      const userData = this.userForm.value;

      this.authService.register(userData).subscribe({
        next: (user) => {
          this.success = 'Usuario registrado exitosamente';
          this.closeForm();
          this.loading = false;
          this.loadUsers(); // Reload users list
        },
        error: (error) => {
          console.error('Error registering user:', error);
          this.error = error.error?.message || 'Error al registrar el usuario';
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
    return this.editingUser !== null;
  }
}