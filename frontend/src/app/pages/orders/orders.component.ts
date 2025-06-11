import {Component, QueryList, ViewChildren} from '@angular/core';
import {CardComponent} from '../../components/card/card.component';
import {AccordionComponent} from '../../components/accordion/accordion.component';
import {MatButton} from '@angular/material/button';
import {MatCheckbox} from '@angular/material/checkbox';

@Component({
  selector: 'app-orders',
  imports: [
    CardComponent,
    AccordionComponent,
    MatButton,
    MatCheckbox,
  ],
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.css'
})
export class OrdersComponent {

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
  checkBox(event: MouseEvent, article: any) {
    event.stopPropagation();
    article.checked = !article.checked;
    this.allChecked = this.isAllBoxesChecked();
  }

  // tracks how many articles are checked
  checkedCount = () => {
    return this.orders.flatMap(order => order.articles).filter(article => article.checked).length;
  }

  // Alle auswählen checkbox
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


  // Data structure to hold articles of all orders
  // and mapping them to accordions & checkboxes each
  orders = [
    {
      title: "Order 1",
      articles: [
        {
          title: "Tobii Expo/iCan Application",
          id: 3605,
          price: 900.00,
          company: "Tobii Technology AB",
          orderer: "Orderer 1",
          checked: false,
        }
      ]
    },
    {
      title: "Order 2",
      articles: [
        {
          title: "Creative Suite 4 Premium",
          id: 3606,
          price: 627.99,
          company: "Adobe (asknet)",
          orderer: "Orderer 1",
          checked: false,
        }
      ]
    }
  ]

}
