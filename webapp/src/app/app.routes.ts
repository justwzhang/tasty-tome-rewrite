import { Routes } from '@angular/router';
import { authGuard } from './core/auth/auth-guard';

export const routes: Routes = [
  // {
  //   path: '**',
  //   loadComponent: () => import('./app').then(m => m.App),
  //   runGuardsAndResolvers: 'always' // Re-trigger guards after auth callback
  // },
  
  // // Your main app route with authentication guard
  // {
  //   path: '',
  //   loadComponent: () => import('./app').then(m => m.App),
  //   canActivate: [authGuard],
  //   runGuardsAndResolvers: 'always'
  // }

];
