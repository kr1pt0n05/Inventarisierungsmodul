import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, GuardResult, MaybeAsync, RouterStateSnapshot} from '@angular/router';
import {AuthenticationService} from './authentication.service';

interface CanActivate {
}

@Injectable({
  providedIn: 'root'
})
export class DefaultGuardService implements CanActivate{

  constructor(private authService: AuthenticationService) {
    this.authService = authService;
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): MaybeAsync<GuardResult>{
    return this.authService.validToken();
  }
}
