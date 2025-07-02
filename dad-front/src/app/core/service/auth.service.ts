import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { tap, takeUntil } from 'rxjs/operators';
import { Services, AuthResponse, LoginRequest, AuthUser } from './services';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private tokenKey = 'auth_token';
  private userKey = 'current_user';
  private currentUserSubject = new BehaviorSubject<string | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  private destroy$ = new Subject<void>();

  constructor(private services: Services) {
    // Initialize with current user if exists
    const currentUser = this.getCurrentUser();
    const token = this.getToken();
    console.log('AuthService constructor - user:', currentUser, 'token exists:', !!token);
    
    if (currentUser && token) {
      this.currentUserSubject.next(currentUser);
    }
  }

  login(loginRequest: LoginRequest): Observable<AuthResponse> {
    return this.services.login(loginRequest).pipe(
      tap(response => {
        console.log('Login response:', response);
        this.setToken(response.token);
        this.setCurrentUser(response.userName);
        this.currentUserSubject.next(response.userName);
        console.log('User set in service:', response.userName);
      })
    );
  }

  register(user: AuthUser): Observable<AuthUser> {
    return this.services.register(user);
  }

  logout(): void {
    // Cancel all pending requests
    this.destroy$.next();
    this.destroy$.complete();
    // Recreate the destroy subject for future use
    this.destroy$ = new Subject<void>();
    
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userKey);
    this.currentUserSubject.next(null);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  getCurrentUser(): string | null {
    return localStorage.getItem(this.userKey);
  }

  private setToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  private setCurrentUser(userName: string): void {
    localStorage.setItem(this.userKey, userName);
  }
}