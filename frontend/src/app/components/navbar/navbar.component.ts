import { Component } from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import {routes} from '../../app.routes';

@Component({
  selector: 'app-navbar',
  imports: [RouterOutlet, RouterLink],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  protected readonly routes = routes;
}
