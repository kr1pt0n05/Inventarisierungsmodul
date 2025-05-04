import {Component, input, signal} from '@angular/core';
import {CardComponent} from '../../components/card/card.component';
import {ChipComponent} from '../../components/chip/chip.component';
import {RangeSliderComponent} from '../../components/range-slider/range-slider.component';
import {DatepickerComponent} from '../../components/datepicker/datepicker.component';

@Component({
  selector: 'app-inventory',
  imports: [
    CardComponent,
    ChipComponent,
    RangeSliderComponent,
    DatepickerComponent
  ],
  templateUrl: './inventory.component.html',
  styleUrl: './inventory.component.css'
})
export class InventoryComponent {

}
