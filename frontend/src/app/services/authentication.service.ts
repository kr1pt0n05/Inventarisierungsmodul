import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';
import { authCodeFlowConfig } from '../app.config';
import { OrderService } from './order.service';


/**
 * AuthenticationService
 *
 * This Angular service handles the authentication and authorization logic using OAuth2 with OpenID Connect.
 * It integrates with the `angular-oauth2-oidc` library to manage token-based authentication and provides methods for login, logout, and token validation.
 *
 * Features:
 * - Configures the OAuth2 service with the authorization server settings.
 * - Initializes the OAuth2 code flow for login and automatic token refresh.
 * - Provides methods for logging in, logging out, and checking token validity.
 *
 * Dependencies:
 * - `OAuthService`: A service from `angular-oauth2-oidc` for managing OAuth2 authentication.
 * - `authCodeFlowConfig`: Configuration for the OAuth2 authorization code flow.
 */
@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {


  /**
   * Constructor that injects the OAuthService and initializes the authentication flow.
   *
   * @param oauthService - The OAuth2 service instance used for handling authentication.
   */
  constructor(public oauthService: OAuthService,
    private readonly router: Router,
    private readonly orderService: OrderService) {
    // Configure the OAuth2 service with the settings from the configuration file.
    this.oauthService.configure(authCodeFlowConfig);

    // Initialize authentication flow outside of constructor.
    this.initializeAuthentication();

    // Set up automatic token refresh to ensure the user's session stays active.
    // This refresh happens when 75% of the access token's lifetime is used (this can be adjusted).
    this.oauthService.setupAutomaticSilentRefresh();
  }

  /**
   * Initializes the authentication flow by loading the discovery document and trying to log in.
   * If a redirect URL is present in the state, navigates to that URL.
   */
  private initializeAuthentication(): void {
    this.oauthService.loadDiscoveryDocumentAndTryLogin().then(() => {
      const url = this.oauthService.state;

      this.orderService.openArticlesChanged.next(); // Trigger the initial load of open articles
      if (url) {
        this.router.navigateByUrl(decodeURIComponent(url));
      }
    });
  }



  /**
   * Initiates the login process by starting the OAuth2 authorization code flow.
   * This method redirects the user to the authorization server for authentication.
   */
  login(): void {
    this.oauthService.initCodeFlow(window.location.pathname);
  }

  /**
   * Logs the user out by clearing the session and redirecting to the logout endpoint.
   */
  logout(): void {
    this.oauthService.logOut();
  }

  /**
   * Checks if the user has valid authentication tokens.
   * A valid token means the user is authenticated and authorized.
   *
   * @returns {boolean} - Returns true if both the ID token and access token are valid, false otherwise.
   */
  validToken(): boolean {
    return (this.oauthService.hasValidIdToken() && this.oauthService.hasValidAccessToken());
  }

  /**
   * Retrieves the username of the authenticated user.
   *
   * @returns {string} - Returns the username of the authenticated user.
   * If the username is not available, it returns an empty string.
   */
  getUsername(): string {
    return this.oauthService.getIdentityClaims()?.['name'] ?? '';
  }
}
