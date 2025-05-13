import { Routes } from '@angular/router';
import {InventoryComponent} from './pages/inventory/inventory.component';
import {HomepageComponent} from './pages/homepage/homepage.component';
import {NotFoundComponent} from './pages/not-found/not-found.component';
import {LoginComponent} from './pages/login/login.component';

export const routes: Routes = [
  {
    title: 'Login',
    path: 'login',
    component: LoginComponent,
  },
  {
    title: 'Home',
    path: '',
    component: HomepageComponent,
  },
  {
    title: 'Inventarliste',
    path: 'inventory',
    component: InventoryComponent,
  },
  {
    title: '404 Not Found!',
    path: '**',
    component: NotFoundComponent,
  }
];
