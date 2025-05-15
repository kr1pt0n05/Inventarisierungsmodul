import { Component } from '@angular/core';
import {AuthenticationService} from '../../services/authentication.service';

@Component({
  selector: 'app-login',
  imports: [
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  constructor(public authService: AuthenticationService) {
    this.authService = authService;
  }
}
