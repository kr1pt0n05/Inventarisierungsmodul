import {Component, OnInit, signal} from '@angular/core';
import {RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [
    RouterLink,
    RouterLinkActive,
    RouterOutlet,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  currentPageTitle = signal<string>("Homepage");

  updateCurrentPageTitle(currentPageTitle: string) {
    this.currentPageTitle.set(currentPageTitle);
  }

}
