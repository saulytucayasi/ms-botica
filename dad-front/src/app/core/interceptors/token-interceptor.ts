import { HttpInterceptorFn } from '@angular/common/http';

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  //const token = localStorage.getItem('token');
  const token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJub2UudGlwbyIsImlkIjoxLCJpYXQiOjE3NTAyNTQ2NDgsImV4cCI6MTc1MDI1ODI0OH0.w6QMOaAyj5w6ZI8czkNy6wVtYuAzJ3-hiGjDP8FYny8";

  if (token) {
    const authReq = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
    return next(authReq);
  }

  return next(req);
};
