import {ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {tokenInterceptor} from './core/interceptors/token-interceptor';
import {catchInterceptor} from './core/interceptors/catch-interceptor';
import {urlInterceptor} from './core/interceptors/url-interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([
        tokenInterceptor,
        urlInterceptor,
        catchInterceptor
      ])
    )
  ]
};
