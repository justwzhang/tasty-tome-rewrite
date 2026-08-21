import { ApplicationConfig, inject, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideOAuthClient } from 'angular-oauth2-oidc';
import { AuthService } from './core/auth/auth-servce';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes), provideClientHydration(withEventReplay()),
    provideHttpClient(withInterceptors([
      (req, next)=>{
        const authService = inject(AuthService);

        const token = authService.getAccessToken();
        if (!token) return next(req);

        const cloned = req.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`
          }
        });

        return next(req);
            }
    ])),
    provideOAuthClient()
    // provideAuth({
    //     config: {
    //     authority: 'https://auth-zhang-server-pc.duckdns.org/application/o/tasty-tome/',
    //     redirectUrl: 'http://localhost:4200/',
    //     postLogoutRedirectUri: 'http://localhost:4200/',
    //     clientId: 'iC2yMcuFrcx44gNjYwsks1ySoojlWwqX7TSK3nRb',
    //     scope: 'openid profile email offline_access',
    //     responseType: 'code',
    //     silentRenew: true,
    //     useRefreshToken: true,
    //     logLevel: LogLevel.Debug,
    
    //   }
    // })
  ]
};
