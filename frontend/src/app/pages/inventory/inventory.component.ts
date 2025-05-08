import {CardComponent} from '../../components/card/card.component';
import {
  Component,
  QueryList,
  ViewChildren,
} from '@angular/core';
import {ChipComponent} from '../../components/chip/chip.component';
import {RangeSliderComponent} from '../../components/range-slider/range-slider.component';
import {DatepickerComponent} from '../../components/datepicker/datepicker.component';
import {InventoryListComponent} from '../../components/inventory-list/inventory-list.component';
import {SelectMultipleComponent} from '../../components/select-multiple/select-multiple.component';
import {AccordionComponent} from '../../components/accordion/accordion.component';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'app-inventory',
  imports: [
    CardComponent,
    ChipComponent,
    RangeSliderComponent,
    DatepickerComponent,
    InventoryListComponent,
    AccordionComponent,
    MatButton
  ],
  templateUrl: './inventory.component.html',
  styleUrl: './inventory.component.css'
})
export class InventoryComponent {

  @ViewChildren(AccordionComponent) accordions!: QueryList<AccordionComponent>;

  openAllAccordion(){
    this.accordions.map((accordion: AccordionComponent) => {
      accordion.matAccordion.openAll();
    })
  }

  closeAllAccordions(){
    this.accordions.map((accordion: AccordionComponent) => {
      accordion.matAccordion.closeAll();
    })
  }

  updateKostenstellen(data: string[]) {
    console.table(data);
  }
}
