import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-login',
  imports: [
    MatButtonModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  constructor(public authService: AuthenticationService) {
    this.authService = authService;
  }
}
