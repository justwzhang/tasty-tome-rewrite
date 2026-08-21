import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core"
import { OAuthService } from "angular-oauth2-oidc"

@Injectable({
    providedIn:'root'
})
export class AuthService {
    readonly oauthService = inject(OAuthService);
    readonly http = inject(HttpClient);
    login(): void {
        if (!this.oauthService.hasValidAccessToken()) {
            this.oauthService.initLoginFlow();
        }
    }

    logout(): void {
        this.oauthService.logOut();
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
        }
        return null;
    }
}