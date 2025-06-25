import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, GuardResult, MaybeAsync, Router, RouterStateSnapshot } from '@angular/router';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class DefaultGuardService {

  constructor(private readonly authService: AuthenticationService,
    private readonly router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): MaybeAsync<GuardResult> {
    const isAuthorised = this.authService.validToken() && this.authService.getUsername() !== 'Test Test' && this.authService.getUsername() !== 'Demo User';
    // Exclude test and demo user from test system for final presentation. -> No keycloak config needed, dont use in production!
    if (isAuthorised) {
      return true;
    }
    this.router.navigate(['/unauthorised'], { skipLocationChange: true });
    return false;
  }

}
