import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {CardComponent} from '../../components/card/card.component';
import {AccordionComponent} from '../../components/accordion/accordion.component';
import {MatButton} from '@angular/material/button';
import {MatCheckbox} from '@angular/material/checkbox';
import { Router } from '@angular/router';
import {NgClass} from '@angular/common';
import {OrderService} from '../../services/order.service';
import {Order} from '../../models/Order';

@Component({
  selector: 'app-orders',
  imports: [
    CardComponent,
    AccordionComponent,
    MatButton,
    MatCheckbox,
    NgClass,
  ],
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.css'
})
export class OrdersComponent implements OnInit{

  constructor(private readonly router: Router, private readonly orderService: OrderService) {
  }

  // Orders
  orders: Order[] = [];

  // get all active orders
  ngOnInit(): void {
    // Fetch orders and add checked attribute to each article, to track which orders the user has selected for inventorization
    this.orderService.getOpenOrders().subscribe(orders => {
      this.orders = orders.map(order => ({
        ...order,
        articles: order.articles.map(article => ({
          ...article,
          checked: false
        }))
      }))
    });
  }

  // Accordion
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

  // This will stop the Accordion from expanding when the checkbox is clicked
  // and check or uncheck the clicked check box
  checkBox(event: MouseEvent, article: any) {
    event.stopPropagation();
    article.checked = !article.checked;
    this.allChecked = this.isAllBoxesChecked();
  }

  // tracks how many articles are checked
  checkedCount = () => {
    return this.orders.flatMap(order => order.articles).filter(article => article.checked).length;
  }

  // "Alle auswählen" checkbox
  allChecked = false;

  // Returns whether all check boxes are checked
  isAllBoxesChecked(): boolean {
    let allChecked = true;
    this.orders.forEach(order => {
      order.articles.forEach(article => {
        if (!article.checked) {
          allChecked = false;
        }
      })
    })
    this.checkedCount();
    return allChecked;
  }

  // Check/uncheck all check boxes
  checkAll() {
    this.allChecked = !this.allChecked;
    this.orders.forEach(order => {
      order.articles.forEach(article => {
        article.checked = this.allChecked;
      })
    })
    this.checkedCount();
  }


  // "Jetzt inventarisieren" Button
  // Will redirect to Details Page for inventarizing selected articles
  inventarize() {
    const checkedOrders: any[] = this.orders.flatMap(order => order.articles).filter(article => article.checked);
    if(checkedOrders.length === 1){
      this.router.navigate(['/inventory/', checkedOrders[0].id]);
    }
    //this.router.navigate(['/inventory/']);
  }

}
