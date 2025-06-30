import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from './core/service/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {
  title = 'Sistema de Gestión de Farmacia';
  currentUser: string | null = null;
  isAuthenticated = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // Initialize state
    this.updateAuthState();
    
    // Subscribe to authentication changes
    this.authService.currentUser$.subscribe(user => {
      console.log('App - User changed:', user);
      this.updateAuthState();
      this.cdr.detectChanges();
    });
  }

  private updateAuthState(): void {
    this.currentUser = this.authService.getCurrentUser();
    this.isAuthenticated = this.authService.isAuthenticated();
    console.log('App - Updated state:', { user: this.currentUser, authenticated: this.isAuthenticated });
  }

  logout(): void {
    console.log('App - Logout clicked');
    this.authService.logout();
    this.updateAuthState();
    this.router.navigate(['/login']).then(() => {
      console.log('App - Navigated to login');
    });
  }

  navigateTo(route: string): void {
    this.router.navigate([route]);
  }
}
