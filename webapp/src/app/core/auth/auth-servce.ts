import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { inject, Injectable, signal } from "@angular/core"
import { OAuthService } from "angular-oauth2-oidc"

export interface CurrentUser {
    userId: number;
    firstName: string;
    lastName: string;
    email: string;
}

@Injectable({
    providedIn:'root'
})
export class AuthService {
    readonly oauthService = inject(OAuthService);
    readonly http = inject(HttpClient);
    readonly currentUser = signal<CurrentUser | null>(null);

    login(): void {
        if (!this.oauthService.hasValidAccessToken()) {
            this.oauthService.initLoginFlow();
        }
    }

    logout(): void {
        this.currentUser.set(null);
        this.oauthService.logOut();
    }

    loadCurrentUser(): void {
        const token = this.getAccessToken();
        if (!token) {
            this.currentUser.set(null);
            return;
        }

        const headers = { Authorization: `Bearer ${token}` };

        this.http.get<CurrentUser>('http://localhost:8080/user/me', { headers }).subscribe({
            next: user => this.currentUser.set(user),
            error: (error: HttpErrorResponse) => {
                console.error('Failed to load current user', error.status, error.error);
                this.currentUser.set(null);
            }
        });
    }

    test(): void {

        const token = this.getAccessToken();
        
        if (token) {
            console.log('access token:', token);
            console.log('id token:', this.oauthService.getIdToken());
            console.log('user info:', this.oauthService.getIdentityClaims());
            this.http.get('http://localhost:8080/user/test', {
                headers: { Authorization: `Bearer ${token}`, responseType: 'text' }
            }).subscribe({
                next: (value) => console.log(value),
                error: (err) => console.error(err)
            });
        } else {
            console.log('No valid access token available');
        }
    }
    getAccessToken(): string | null {
        if (this.oauthService.hasValidAccessToken()) {
            return this.oauthService.getAccessToken();
        } else{
            this.login();
        }
        return null;
    }
}