import { NgClass } from '@angular/common';
import { Component, signal } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthenticationService } from './services/authentication.service';

@Component({
  selector: 'app-root',
  imports: [
    RouterLink,
    RouterLinkActive,
    RouterOutlet,
    NgClass,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  constructor(public authService: AuthenticationService) {
    this.authService = authService;
  }

  currentPageTitle = signal<string>("Homepage");

  updateCurrentPageTitle(currentPageTitle: string) {
    this.currentPageTitle.set(currentPageTitle);
  }

}

export function localizePrize(price: number | string): string {
  let numPrice: number;
  if (typeof price === 'string') {
    numPrice = parseFloat(price);
  } else {
    numPrice = price;
  }
  return numPrice.toLocaleString('de-DE', {
    style: 'currency',
    currency: 'EUR',
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  });
}

export function unLocalizePrize(price: string): number {
  return parseFloat(String(price).replace(',', '.').replace('€', '').trim());
}
