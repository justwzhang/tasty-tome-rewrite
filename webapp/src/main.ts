import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';
import { OAuthService } from 'angular-oauth2-oidc';
import { authentikAuthConfig } from './app/core/auth/authentik-auth';
import { AuthService } from './app/core/auth/auth-servce';


bootstrapApplication(App, appConfig).then(async (appRef) => {
  const oauthService = appRef.injector.get(OAuthService);
  oauthService.configure(authentikAuthConfig);
  await oauthService.loadDiscoveryDocumentAndTryLogin();
  appRef.injector.get(AuthService).loadCurrentUser();
}).catch((err) => console.error(err));