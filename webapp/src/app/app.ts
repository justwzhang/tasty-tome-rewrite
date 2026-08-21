import { Component, computed, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { authGuard } from './core/auth/auth-guard';
import { AuthService } from './core/auth/auth-servce';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  template: `
    <button (click)="login()">Login</button>
    <button (click)="logout()">Logout</button>
    <button (click)="test()">Test</button>
    <router-outlet></router-outlet>
  `,
})
export class App {
  private readonly oidc = inject(AuthService);
  
  constructor() {
    // const isAuthenticated = this.oidc.getAccessToken();
    // console.log(isAuthenticated);
  }

  login() {
    this.oidc.login();
  }

  logout() {
    // this.oidc.logoff().subscribe((result) => console.log(result));
    this.oidc.logout();
  }
  test(){
    this.oidc.test();
  }
}