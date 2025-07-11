import { Component, signal } from '@angular/core';
import { MatBadgeModule } from '@angular/material/badge';
import { MatButtonModule } from '@angular/material/button';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthenticationService } from './services/authentication.service';
import { OrderService } from './services/order.service';

@Component({
  standalone: true,
  selector: 'app-root',
  imports: [
    RouterLink,
    RouterLinkActive,
    RouterOutlet,
    MatButtonModule,
    MatBadgeModule
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent implements OnInit {
  constructor(public authService: AuthenticationService,
    private readonly router: Router,
    private readonly orderService: OrderService) {
    // Initialize the current page title based on the current route
    this.updateCurrentPageTitle(this.router.url === '/' ? 'Startseite' : this.router.url.replace('/', ''));
  }

  currentPageTitle = signal<string>("Startseite");

  buttonClasses = 'w-fit rounded-md px-3 py-2 text-md font-medium text-gray-300 hover:bg-gray-700 hover:text-white';
  buttonActiveClasses = 'bg-gray-900 text-white';

  showMobileMenu = signal<boolean>(false);

  numberOfOpenArticles = signal<number>(0);
  ngOnInit() {
    this.orderService.getNumberOfOpenArticles().subscribe((count) => {
      this.numberOfOpenArticles.set(count);
    });

    // Subscribe to changes in open articles to keep the value updated
    this.orderService.openArticlesChanged.subscribe(() => {
      this.orderService.getNumberOfOpenArticles().subscribe((count) => {
        this.numberOfOpenArticles.set(count);
      });
    });
  }

  updateCurrentPageTitle(currentPageTitle: string) {
    this.currentPageTitle.set(currentPageTitle);
  }

  handleLogInOut() {
    if (this.authService.validToken()) {
      this.authService.logout()
      this.router.navigate(['/']).then(() => {
        this.updateCurrentPageTitle("Startseite");
      });
    } else {
      this.authService.login();
    }
  }

  toggleMobileMenu() {
    this.showMobileMenu.set(!this.showMobileMenu());
  }

}


/**
 * Converts a price value to a localized string representation with a comma as decimal separator
 * and appends the Euro sign.
 *
 * @param price - The price value as a number or string.
 * @returns The localized price string (e.g., "12,34 €").
 */
export function localizePrice(price: number | string): string {
  let numPrice: number;
  if (typeof price !== 'number') {
    if (price === '' || isNaN(parseFloat(price))) {
      return '';
    }
    numPrice = parseFloat(price);
  } else {
    numPrice = price;
  }
  return numPrice.toString().replace('.', ',').concat('\xa0€').trim();
}

/**
 * Converts a localized price string (with comma as decimal separator and Euro sign)
 * back to a number.
 *
 * @param price - The localized price string (e.g., "12,34 €").
 * @returns The numeric price value.
 */
export function unLocalizePrice(price: string): number {
  return parseFloat(String(price).replace(',', '.').replace('€', '').trim());
}
