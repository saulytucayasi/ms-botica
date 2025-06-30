import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';
import { inject } from '@angular/core';
import { Router } from '@angular/router';

export const catchInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      console.error('Error HTTP:', error);
      
      // Handle authentication errors
      if (error.status === 401 || error.status === 403) {
        // Redirect to login for auth errors
        localStorage.removeItem('auth_token');
        localStorage.removeItem('current_user');
        router.navigate(['/login']);
        return throwError(() => error);
      }
      
      // Don't show alerts for certain errors during logout
      const currentUrl = router.url;
      if (currentUrl === '/login' || error.status === 400) {
        console.log('Ignoring error during logout/login:', error.message);
        return throwError(() => error);
      }
      
      // Show alert for other errors
      alert(`Error ${error.status}: ${error.statusText}`);
      return throwError(() => error);
    })
  );
};
