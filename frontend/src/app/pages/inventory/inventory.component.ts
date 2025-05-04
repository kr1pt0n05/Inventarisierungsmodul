import { Component } from '@angular/core';
import {CardComponent} from '../../card/card.component';

@Component({
  selector: 'app-inventory',
  imports: [
    CardComponent
  ],
  templateUrl: './inventory.component.html',
  styleUrl: './inventory.component.css'
})
export class InventoryComponent {

}
