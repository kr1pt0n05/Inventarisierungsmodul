import { Component, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthenticationService } from './services/authentication.service';

@Component({
  selector: 'app-root',
  imports: [
    RouterLink,
    RouterLinkActive,
    RouterOutlet,
    MatButtonModule
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  constructor(public authService: AuthenticationService,
    private readonly router: Router) {
  }

  currentPageTitle = signal<string>("Startseite");

  buttonClasses = 'w-fit rounded-md px-3 py-2 text-md font-medium text-gray-300 hover:bg-gray-700 hover:text-white';
  buttonActiveClasses = 'bg-gray-900 text-white';

  showMobileMenu = signal<boolean>(false);

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
  if (typeof price === 'string') {
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
