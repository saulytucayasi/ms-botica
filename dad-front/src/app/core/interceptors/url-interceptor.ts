import {HttpInterceptorFn} from '@angular/common/http';
import {environment} from '../../../environments/environment';

export const urlInterceptor: HttpInterceptorFn = (req, next) => {
  if (!req.url.startsWith('http')) {
    // Remove leading slash from URL to avoid double slashes
    const cleanUrl = req.url.startsWith('/') ? req.url.slice(1) : req.url;
    const apiReq = req.clone({
      url: `${environment.apiUrl}/${cleanUrl}`
    });
    return next(apiReq);
  }

  return next(req);
};
