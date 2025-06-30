import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Services, AuthResponse, LoginRequest, AuthUser } from './services';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private tokenKey = 'auth_token';
  private userKey = 'current_user';
  private currentUserSubject = new BehaviorSubject<string | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private services: Services) {
    // Initialize with current user if exists
    const currentUser = this.getCurrentUser();
    if (currentUser && this.getToken()) {
      this.currentUserSubject.next(currentUser);
    }
  }

  login(loginRequest: LoginRequest): Observable<AuthResponse> {
    return this.services.login(loginRequest).pipe(
      tap(response => {
        console.log('AuthService - Setting user:', response.userName);
        this.setToken(response.token);
        this.setCurrentUser(response.userName);
        this.currentUserSubject.next(response.userName);
        console.log('AuthService - isAuthenticated:', this.isAuthenticated());
        console.log('AuthService - getCurrentUser:', this.getCurrentUser());
      })
    );
  }

  register(user: AuthUser): Observable<AuthUser> {
    return this.services.register(user);
  }

  logout(): void {
    console.log('AuthService - Logout called');
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userKey);
    this.currentUserSubject.next(null);
    console.log('AuthService - After logout:', {
      token: this.getToken(),
      user: this.getCurrentUser(),
      isAuthenticated: this.isAuthenticated()
    });
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