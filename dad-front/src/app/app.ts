import { Component, OnInit, ChangeDetectorRef, HostListener } from '@angular/core';
import { RouterOutlet, Router, NavigationEnd } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from './core/service/auth.service';
import { IconComponent } from './shared/components/icons/icon.component';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule, IconComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {
  title = 'Botica Santa Cruz';
  currentUser: string | null = null;
  isAuthenticated = false;
  currentRoute: string = '';
  cartItemCount: number = 0;
  dropdownOpen = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // Initialize state first
    this.initializeAuthState();
    
    // Subscribe to authentication changes
    this.authService.currentUser$.subscribe(user => {
      console.log('Auth state changed, user:', user);
      this.currentUser = user;
      this.isAuthenticated = this.authService.isAuthenticated();
      this.cdr.detectChanges();
    });

    // Track current route for active nav highlighting
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      this.currentRoute = event.url;
    });

    // Initialize current route
    this.currentRoute = this.router.url;
  }

  private initializeAuthState(): void {
    const user = this.authService.getCurrentUser();
    const isAuth = this.authService.isAuthenticated();
    
    console.log('Initializing auth state - user:', user, 'isAuth:', isAuth);
    
    this.currentUser = user;
    this.isAuthenticated = isAuth;
    
    // If we have a user but the subject hasn't been initialized, set it
    if (user && isAuth) {
      this.authService.currentUser$.subscribe(currentSubjectUser => {
        if (!currentSubjectUser) {
          console.log('Setting user in subject:', user);
          // Need to trigger the subject with current user
        }
      });
    }
  }

  logout(): void {
    // Clear auth state immediately
    this.currentUser = null;
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

  toggleDropdown(): void {
    this.dropdownOpen = !this.dropdownOpen;
  }

  navigateToAndClose(route: string): void {
    this.dropdownOpen = false;
    this.router.navigate([route]);
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: Event): void {
    const target = event.target as HTMLElement;
    const dropdown = target.closest('.dropdown');
    
    if (!dropdown && this.dropdownOpen) {
      this.dropdownOpen = false;
    }
  }
}
