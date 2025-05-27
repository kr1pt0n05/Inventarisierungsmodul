import { Routes } from '@angular/router';
import {InventoryComponent} from './pages/inventory/inventory.component';
import {HomepageComponent} from './pages/homepage/homepage.component';
import {NotFoundComponent} from './pages/not-found/not-found.component';
import {LoginComponent} from './pages/login/login.component';
import {DefaultGuardService} from './services/default-guard.service';

export const routes: Routes = [
  {
    title: 'Login',
    path: '',
    component: LoginComponent,

  },
  {
    title: 'Home',
    path: 'homepage',
    component: HomepageComponent,
    canActivate: [DefaultGuardService],
  },
  {
    title: 'Inventarliste',
    path: 'inventory',
    component: InventoryComponent,
    canActivate: [DefaultGuardService],
  },
  {
    title: '404 Not Found!',
    path: '**',
    component: NotFoundComponent,
  }
];
