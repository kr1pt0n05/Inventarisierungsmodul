import { Injectable } from '@angular/core';
import {OAuthService} from 'angular-oauth2-oidc';
import {authCodeFlowConfig} from '../app.config';


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
  constructor(public oauthService: OAuthService) {
    // Configure the OAuth2 service with the settings from the configuration file.
    this.oauthService.configure(authCodeFlowConfig);

    // Load the discovery document and try logging the user in (if a valid token exists).
    this.oauthService.loadDiscoveryDocumentAndTryLogin();

    // Set up automatic token refresh to ensure the user's session stays active.
    // This refresh happens when 75% of the access token's lifetime is used (this can be adjusted).
    this.oauthService.setupAutomaticSilentRefresh();
  }

  /**
   * Initiates the login process by starting the OAuth2 authorization code flow.
   * This method redirects the user to the authorization server for authentication.
   */
  login(): void {
    this.oauthService.initCodeFlow();
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
}

