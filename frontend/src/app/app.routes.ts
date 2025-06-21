import { Routes } from '@angular/router';
import { DetailsComponent } from './pages/details/details.component';
import { ExtensionInventorizationComponent } from './pages/extension-inventorization/extension-inventorization.component';
import { HomepageComponent } from './pages/homepage/homepage.component';
import { InventorizationComponent } from './pages/inventorization/inventorization.component';
import { InventoryComponent } from './pages/inventory/inventory.component';
import { LoginComponent } from './pages/login/login.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { OrdersComponent } from './pages/orders/orders.component';
import { InventoryItemChangesResolver } from './resolver/changes.resolver';
import { CommentsResolver } from './resolver/comments.resolver';
import { ExtensionResolver } from './resolver/extension.resolver';
import { ExtensionsResolver } from './resolver/extensions.resolver';
import { InventoryItemResolver } from './resolver/inventory-item.resolver';
import { DefaultGuardService } from './services/default-guard.service';
import {StatisticsComponent} from './pages/statistics/statistics.component';


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
    canActivate: [DefaultGuardService],
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
    canActivate: [DefaultGuardService],
  },
  {
    title: 'Erweiterung bearbeiten',
    path: 'edit/:inventoryId/extension/:extensionId',
    component: ExtensionInventorizationComponent,
    canActivate: [DefaultGuardService],
    resolve: {
      isNewExtension: () => false,
      inputExtension: ExtensionResolver
    }
  },
  {
    title: 'Inventarisierung bearbeiten',
    path: 'edit/:id',
    component: InventorizationComponent,
    canActivate: [DefaultGuardService],
    resolve: {
      inventoryItem: InventoryItemResolver,
      isNewInventorization: () => false,
    },
  },
  {
    title: 'Neue Inventarisierung',
    path: 'new',
    component: InventorizationComponent,
    canActivate: [DefaultGuardService],
    resolve: {
      isNewInventorization: () => true,
      inventoryItem: () => ({}),
    }
  },
  {
    title: 'Neue Erweiterung',
    path: 'new-extension',
    component: ExtensionInventorizationComponent,
    canActivate: [DefaultGuardService],
    resolve: {
      isNewExtension: () => true,
    }

  },

  {
    title: 'Statistiken',
    path: 'statistics',
    component: StatisticsComponent,
    canActivate: [DefaultGuardService],
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
