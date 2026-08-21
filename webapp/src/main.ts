import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';
import { OAuthService } from 'angular-oauth2-oidc';
import { authentikAuthConfig } from './app/core/auth/authentik-auth';


bootstrapApplication(App, appConfig).then((appRef) => {
  const oauthService = appRef.injector.get(OAuthService);
  oauthService.configure(authentikAuthConfig);
  oauthService.loadDiscoveryDocumentAndTryLogin();
}).catch((err) => console.error(err));