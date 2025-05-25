import { Routes } from '@angular/router';
import { DetailsComponent } from './pages/details/details.component';
import { HomepageComponent } from './pages/homepage/homepage.component';
import { InventoryComponent } from './pages/inventory/inventory.component';
import { LoginComponent } from './pages/login/login.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { InventoryItemChangesResolver } from './resolver/changes.resolver';
import { ExtensionsResolver } from './resolver/extensions.resolver';
import { InventoryItemResolver } from './resolver/inventory-item.resolver';
import { InventoryItemNotesResolver } from './resolver/notes.resolver';
import { DefaultGuardService } from './services/default-guard.service';

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
    canActivate: [DefaultGuardService],
  },
  {
    title: 'Inventarliste',
    path: 'inventory',
    component: InventoryComponent,
  },
  { // For testing purposes, should be replaced by dynamic routing based on selected item
    title: 'tmp Details',
    path: 'details',
    canActivate: [DefaultGuardService],
    component: DetailsComponent
  },
  {
    title: 'Inventar',
    path: 'inventory/:id',
    component: DetailsComponent,
    resolve: {
      inventoryItem: InventoryItemResolver,
      extensions: ExtensionsResolver,
      notes: InventoryItemNotesResolver,
      changes: InventoryItemChangesResolver
    }
  },
  {
    title: '404 Not Found!',
    path: '**',
    component: NotFoundComponent,
  }
];
