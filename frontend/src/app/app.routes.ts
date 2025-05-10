import { Routes } from '@angular/router';
import {InventoryComponent} from './pages/inventory/inventory.component';
import {HomepageComponent} from './pages/homepage/homepage.component';
import {NotFoundComponent} from './pages/not-found/not-found.component';
import { DetailsComponent } from './pages/details/details.component';

export const routes: Routes = [
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
  { // For testing purposes, should be replaced by dynamic routing based on selected item
    title: 'tmp Details',
    path: 'details',
    component: DetailsComponent
  },
  {
    title: '404 Not Found!',
    path: '**',
    component: NotFoundComponent,
  }
];
