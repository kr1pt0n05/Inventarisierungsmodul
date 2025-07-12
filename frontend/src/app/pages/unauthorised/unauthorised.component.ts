import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-unauthorised',
  imports: [
    MatButtonModule
  ],
  templateUrl: './unauthorised.component.html',
  styleUrl: './unauthorised.component.css'
})
export class UnauthorisedComponent {
  constructor(public authService: AuthenticationService) { }

}
