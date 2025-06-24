import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, GuardResult, MaybeAsync, RouterStateSnapshot } from '@angular/router';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class DefaultGuardService {

  constructor(private readonly authService: AuthenticationService) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): MaybeAsync<GuardResult> {
    return this.authService.validToken() && this.authService.getUsername() !== 'Test Test' && this.authService.getUsername() !== 'Demo User';
    // Exclude test and demo user from test system for final presentation. -> No keycloak config needed, dont use in production!
  }

}
