import { Routes } from '@angular/router';
import { DetailsComponent } from './pages/details/details.component';
import { HomepageComponent } from './pages/homepage/homepage.component';
import { InventorizationComponent } from './pages/inventorization/inventorization.component';
import { InventoryComponent } from './pages/inventory/inventory.component';
import { LoginComponent } from './pages/login/login.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { InventoryItemChangesResolver } from './resolver/changes.resolver';
import { CommentsResolver } from './resolver/comments.resolver';
import { ExtensionsResolver } from './resolver/extensions.resolver';
import { InventoryItemResolver } from './resolver/inventory-item.resolver';
import { DefaultGuardService } from './services/default-guard.service';
import {OrdersComponent} from './pages/orders/orders.component';

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
    title: 'Inventar',
    path: 'inventory/:id',
    component: DetailsComponent,
    resolve: {
      inventoryItem: InventoryItemResolver,
      extensions: ExtensionsResolver,
      comments: CommentsResolver,
      changes: InventoryItemChangesResolver,
    }
  },
  {
    title: "Bestellungen",
    path: "orders",
    component: OrdersComponent,
  },
  {
    title: 'Inventarisierung',
    path: 'edit/:id',
    component: InventorizationComponent,
    resolve: {
      inventoryItem: InventoryItemResolver,
    },
  },
  {
    title: '404 Not Found!',
    path: '**',
    component: NotFoundComponent,
  }
];
