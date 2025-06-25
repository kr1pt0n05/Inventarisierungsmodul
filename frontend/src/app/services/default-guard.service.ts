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
    if (this.authService.validToken()) {
      return true;
    }
    this.router.navigate(['/unauthorised'], { skipLocationChange: true });
    return false;
  }

}
