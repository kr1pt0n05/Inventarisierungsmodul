import { Routes } from '@angular/router';
import { DetailsComponent } from './pages/details/details.component';
import { ExtensionInventorizationComponent } from './pages/extension-inventorization/extension-inventorization.component';
import { HomepageComponent } from './pages/homepage/homepage.component';
import { InventorizationComponent } from './pages/inventorization/inventorization.component';
import { InventoryComponent } from './pages/inventory/inventory.component';
import { LoginComponent } from './pages/login/login.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { OrderizationComponent } from './pages/orderization/orderization.component';
import { OrdersComponent } from './pages/orders/orders.component';
import { InventoryItemChangesResolver } from './resolver/changes.resolver';
import { CommentsResolver } from './resolver/comments.resolver';
import { ExtensionsResolver } from './resolver/extensions.resolver';
import { InventoryItemResolver } from './resolver/inventory-item.resolver';
import { OrderResolverService } from './resolver/order-resolver.service';
import { DefaultGuardService } from './services/default-guard.service';


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
    title: 'Inventarisierung der Bestellungen',
    path: 'orderize/:id',
    component: OrderizationComponent,
    resolve: {
      article: OrderResolverService,
    },
  },
  {
    title: 'Inventarisierung bearbeiten',
    path: 'edit/:id',
    component: InventorizationComponent,
    resolve: {
      inventoryItem: InventoryItemResolver,
      isNewInventorization: () => false,
    },
  },
  {
    title: 'Neue Inventarisierung',
    path: 'new',
    component: InventorizationComponent,
    resolve: {
      isNewInventorization: () => true,
    }
  },
  {
    title: 'Neue Erweiterung',
    path: 'new-extension',
    component: ExtensionInventorizationComponent,
    resolve: {
      isNewExtension: () => true,
    }

  },
  {
    title: '404 Not Found!',
    path: '**',
    component: NotFoundComponent,
  }
];
