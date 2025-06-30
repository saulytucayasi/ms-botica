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
  title = 'Santa Cruz';
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
      this.updateAuthState();
      this.cdr.detectChanges();
    });
  }

  private updateAuthState(): void {
    this.isAuthenticated = this.authService.isAuthenticated();
  }

  logout(): void {
    // Clear auth state immediately
    this.isAuthenticated = false;

    // Perform logout
    this.authService.logout();

    // Navigate to login
    this.router.navigate(['/login'], { replaceUrl: true }).then(() => {
      // Force change detection after navigation
      this.cdr.detectChanges();
    });
  }

  navigateTo(route: string): void {
    this.router.navigate([route]);
  }
}
