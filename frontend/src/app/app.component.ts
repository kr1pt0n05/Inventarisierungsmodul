import { Component, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthenticationService } from './services/authentication.service';

@Component({
  standalone: true,
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


  /**
   * Updates the current page title signal with the provided title.
   * This method is used to set the title of the current page dynamically.
   *
   * @param currentPageTitle - The title to set for the current page.
   */
  updateCurrentPageTitle(currentPageTitle: string): void {
    this.currentPageTitle.set(currentPageTitle);
  }

  /**
   * Handles the login/logout functionality based on the user's authentication status.
   * If the user has a valid token, it logs them out and navigates to the home page.
   * If the user does not have a valid token, it initiates the login process.
   */
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

  /**
   * Toggles the visibility of the mobile menu.
   * This method sets the `showMobileMenu` signal to its opposite value,
   * effectively showing or hiding the mobile menu when called.
   */
  toggleMobileMenu() {
    this.showMobileMenu.set(!this.showMobileMenu());
  }

  /**
   * Generates user initials based on the username from the authentication service.
   * It extracts the first letter of each word in the username, converts them to uppercase,
   * and joins them together.
   *
   * @returns {string} - The initials of the user, or '?' if the username is not available.
   */
  getUserInitials(): string {
    return this.authService.getUsername().match(/\b(\w)/g)?.join('').toUpperCase() ?? '?';
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
