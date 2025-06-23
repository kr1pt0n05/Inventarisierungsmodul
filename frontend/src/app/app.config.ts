import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';

import {
  provideHttpClient,
  withFetch,
  withInterceptorsFromDi
} from '@angular/common/http';
import { MAT_DATE_LOCALE, provideNativeDateAdapter } from '@angular/material/core';
import { AuthConfig, provideOAuthClient } from 'angular-oauth2-oidc';
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes, withComponentInputBinding()),
    provideNativeDateAdapter(),
    { provide: MAT_DATE_LOCALE, useValue: 'de-DE' },
    provideHttpClient(
      withFetch(),
      withInterceptorsFromDi(),
    ),
    provideOAuthClient({
      resourceServer: {
        allowedUrls: ['http://localhost:8080', 'https://http://insy.hs-esslingen.com'],
        sendAccessToken: true,
      }
    })
  ]
};


// Reference: https://www.npmjs.com/package/angular-oauth2-oidc
export const authCodeFlowConfig: AuthConfig = {
  // Url of the Identity Provider
  issuer: 'https://auth.insy.hs-esslingen.com/realms/insy',

  // URL of the SPA to redirect the user to after login
  redirectUri: window.location.origin + '/',

  // The SPA's id. The SPA is registerd with this id at the auth-server
  // clientId: 'server.code',
  clientId: 'angular-app-prod',

  // Just needed if your auth server demands a secret. In general, this
  // is a sign that the auth server is not configured with SPAs in mind
  // and it might not enforce further best practices vital for security
  // such applications.
  // dummyClientSecret: 'secret',

  responseType: 'code',

  // set the scope for the permissions the client should request
  // The first four are defined by OIDC.
  // Important: Request offline_access to get a refresh token
  // The api scope is a usecase specific one
  scope: 'profile email offline_access',

  showDebugInformation: true,
};
