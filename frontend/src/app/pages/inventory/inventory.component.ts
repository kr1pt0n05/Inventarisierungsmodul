import {CardComponent} from '../../components/card/card.component';
import {
  Component, OnInit,
  QueryList,
  ViewChildren,
} from '@angular/core';
import {RangeSliderComponent} from '../../components/range-slider/range-slider.component';
import {DatepickerComponent} from '../../components/datepicker/datepicker.component';
import {InventoryListComponent} from '../../components/inventory-list/inventory-list.component';
import {AccordionComponent} from '../../components/accordion/accordion.component';
import {MatButton} from '@angular/material/button';
import {FormControl, FormGroup} from '@angular/forms';
import {ChipV2Component} from '../../components/chip-v2/chip-v2.component';


@Component({
  selector: 'app-inventory',
  imports: [
    CardComponent,
    RangeSliderComponent,
    DatepickerComponent,
    InventoryListComponent,
    AccordionComponent,
    MatButton,
    ChipV2Component,
  ],
  templateUrl: './inventory.component.html',
  styleUrl: './inventory.component.css'
})
export class InventoryComponent implements OnInit {

  @ViewChildren(AccordionComponent) accordions!: QueryList<AccordionComponent>;

  // Form
  inventoryForm!: FormGroup;

  ngOnInit(): void {
    this.inventoryForm = new FormGroup({
      costCenter: new FormControl([]),
      inventoryNumberMin: new FormControl(''),
      inventoryNumberMax: new FormControl(''),
      productDescription: new FormControl(''),
      company: new FormControl(''),
      priceMin: new FormControl(''),
      priceMax: new FormControl(''),
      dateStart: new FormControl(''),
      dateEnd: new FormControl(''),
      serialNumber: new FormControl(''),
      location: new FormControl(''),
      order: new FormControl(''),
      tags: new FormControl(''),
    })

    this.inventoryForm.valueChanges.subscribe(value => {
      console.log('Name control changed:', value);
    });
  }


  // Accordion
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

}
