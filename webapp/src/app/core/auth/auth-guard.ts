import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';

export const authGuard: CanActivateFn = () => {
  // const oidc = inject(OidcSecurityService);

  // const isAuthenticated = oidc.getAccessToken() !== null;

  // if (!isAuthenticated) {
  //   oidc.authorize();
  //   return false;
  // }

  return true;
};