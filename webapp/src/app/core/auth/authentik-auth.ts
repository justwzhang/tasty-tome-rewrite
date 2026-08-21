import { AuthConfig } from 'angular-oauth2-oidc';

export const authentikAuthConfig: AuthConfig = {
  issuer: 'https://auth-zhang-server-pc.duckdns.org/application/o/tasty-tome/',
  redirectUri: window.location.origin,
  clientId: 'iC2yMcuFrcx44gNjYwsks1ySoojlWwqX7TSK3nRb',
  responseType: 'code',
  scope: 'openid profile email offline_access',
  requireHttps: true,
  strictDiscoveryDocumentValidation: false
};