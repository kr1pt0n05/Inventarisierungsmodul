import { NgClass } from '@angular/common';
import { Component, OnInit, QueryList, ViewChildren } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatCheckbox } from '@angular/material/checkbox';
import { Router } from '@angular/router';
import { AccordionComponent } from '../../components/accordion/accordion.component';
import { CardComponent } from '../../components/card/card.component';
import { Article, Order } from '../../models/Order';
import { OrderService } from '../../services/order.service';

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
export class OrdersComponent implements OnInit {

  constructor(private readonly router: Router, private readonly orderService: OrderService) {
  }

  // Orders
  orders: Order[] = [];
  numberOfArticles = () => {
    return this.orders.flatMap(order => order.articles).length;
  }
  checkedArticles = () => {
    return this.orders.flatMap(order => order.articles).filter(article => article.checked);
  }

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

  openAllAccordion() {
    this.accordions.map((accordion: AccordionComponent) => {
      accordion.matAccordion.openAll();
    })
  }

  closeAllAccordions() {
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


  // "Jetzt inventarisieren" Button'
  /**
   * Inventarize the checked articles in the selected mode.
   * - asItems: Inventarize all checked articles as items.
   * - asExtensions: Inventarize all checked articles as extensions.
   * - addToFirst: Inventarize the first checked article as an item and the rest as extensions of the first article.
   * @param mode - The mode of inventorization: 'asItems', 'asExtensions', or 'addToFirst'.
   */
  inventarize(mode: string) {
    const checkedOrders: Article[] = this.orders.flatMap(order => order.articles).filter(article => article.checked);
    if (checkedOrders.length >= 1) {
      const itemArticles: number[][] = [];
      const extensionArticles: number[][] = [];
      const route = ['/new']
      console.log('Inventarize mode:', mode);
      console.log('Checked Orders:', checkedOrders);
      switch (mode) {
        case 'asItems':
          for (const article of checkedOrders) {
            itemArticles.push([this.orders.find(order => order.articles.includes(article))!.id, article.article_id]);
          }
          break;
        case 'asExtensions':
          for (const article of checkedOrders) {
            extensionArticles.push([this.orders.find(order => order.articles.includes(article))!.id, article.article_id]);
          }
          route[0] = '/new-extension';
          break;
        case 'addToFirst':
          if (checkedOrders.length >= 2) {
            itemArticles.push([this.orders.find(order => order.articles.includes(checkedOrders[0]))!.id, checkedOrders[0].article_id]);
            for (let i = 1; i < checkedOrders.length; i++) {
              extensionArticles.push([this.orders.find(order => order.articles.includes(checkedOrders[i]))!.id, checkedOrders[i].article_id]);
            }
          }
          break;
        default:
          console.error('Unknown mode:', mode);
      }

      this.router.navigate(route, { queryParams: { itemArticles: itemArticles, extensionArticles: extensionArticles } });

    }
  }

}
