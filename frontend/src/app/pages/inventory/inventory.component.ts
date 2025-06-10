import {CardComponent} from '../../components/card/card.component';
import {
  Component, inject, OnInit,
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
import {Filter, ServerTableDataSourceService} from '../../services/server-table-data-source.service';
import {CacheInventoryService} from '../../services/cache-inventory.service';


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

  // Needed for passing the FormGroup as Filter-QueryParameters to the API
  serverTableDataSource = inject(ServerTableDataSourceService);
  cache = inject(CacheInventoryService);

  @ViewChildren(AccordionComponent) accordions!: QueryList<AccordionComponent>;

  // Form
  inventoryForm!: FormGroup;

  costCenters: string[] = [];
  companies: string[] = [];
  serialNumbers: string[] = [];
  locations: string[] = [];
  orderers: string[] = [];
  tags: string[] = [];

  ngOnInit(): void {
    this.inventoryForm = new FormGroup({
      costCenter: new FormControl([]),
      minId: new FormControl(''),
      maxId: new FormControl(''),
      company: new FormControl([]),
      minPrice: new FormControl(''),
      maxPrice: new FormControl(''),
      dateStart: new FormControl(''),
      dateEnd: new FormControl(''),
      serialNumber: new FormControl([]),
      location: new FormControl([]),
      orderer: new FormControl([]),
      tags: new FormControl([]),
    })

    this.serverTableDataSource.filter = this.inventoryForm;
    this.cache.getCostCenters().subscribe(costCenters => this.costCenters = costCenters);
    this.cache.getCompanies().subscribe(companies => this.companies = companies);
    this.cache.getSerialNumbers().subscribe(serialNumbers => this.serialNumbers = serialNumbers.filter(costCenter => costCenter !== null));
    this.cache.getLocations().subscribe(locations => this.locations = locations.filter(costCenter => costCenter !== null));
    this.cache.getOrderers().subscribe(orderers => this.orderers = orderers);
    this.cache.getTags().subscribe(tags => this.tags = tags.filter(costCenter => costCenter !== null));
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
