import { CommonModule } from '@angular/common';
import { Component, OnInit, QueryList, ViewChildren } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatCheckbox } from '@angular/material/checkbox';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Router } from '@angular/router';
import { AccordionComponent } from '../../components/accordion/accordion.component';
import { CardComponent } from '../../components/card/card.component';
import { Article } from '../../models/Article';
import { Order } from '../../models/Order';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-orders',
  imports: [
    CardComponent,
    AccordionComponent,
    MatButton,
    MatCheckbox,
    MatTooltipModule,
    MatExpansionModule,
    CommonModule,

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
  checkedArticles: Article[] = [];

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

      this.orders.forEach(order => {
        order.articles = order.articles.filter(article => !article.is_inventoried);
      });

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
    if (article.checked) {
      this.checkedArticles.push(article);
    } else {
      this.checkedArticles = this.checkedArticles.filter(a => a.article_id !== article.article_id);
    }
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
        if (article.checked && !this.checkedArticles.includes(article)) {
          this.checkedArticles.push(article);
        }
      })
    })
    this.checkedCount();
  }

  isOrderChecked(order: Order): boolean {
    return order.articles.every(article => article.checked);
  }

  checkOrder(event: MouseEvent, order: Order) {
    event.stopPropagation();
    const isChecked = this.isOrderChecked(order);
    order.articles.forEach(article => {
      article.checked = !isChecked;
      if (article.checked && !this.checkedArticles.includes(article)) {
        this.checkedArticles.push(article);
      } else if (!article.checked) {
        this.checkedArticles = this.checkedArticles.filter(a => a.article_id !== article.article_id);
      }
    });
    this.allChecked = this.isAllBoxesChecked();
  }


  // "Jetzt inventarisieren" Button'

  private getArticleOrderId(article: Article): number {
    const order = this.orders.find(order => order.articles.includes(article));
    return order ? order.id : -1;
  }

  private collectArticles(articles: Article[]): number[][] {
    return articles.map(article => [this.getArticleOrderId(article), article.article_id]);
  }

  /**
   * Inventarize the checked articles in the selected mode.
   * - asItems: Inventarize all checked articles as items.
   * - asExtensions: Inventarize all checked articles as extensions.
   * - addToFirst: Inventarize the first checked article as an item and the rest as extensions of the first article.
   * @param mode - The mode of inventorization: 'asItems', 'asExtensions', or 'addToFirst'.
   */
  inventarize(mode: string) {
    if (this.checkedArticles.length < 1) {
      return;
    }

    let itemArticles: number[][] = [];
    let extensionArticles: number[][] = [];
    let route: string[] = [];

    switch (mode) {
      case 'asItems':
        itemArticles = this.collectArticles(this.checkedArticles);
        route.push('/new');
        break;
      case 'asExtensions':
        extensionArticles = this.collectArticles(this.checkedArticles);
        route.push('/new-extension');
        break;
      case 'addToFirst':
        if (this.checkedArticles.length >= 2) {
          itemArticles = this.collectArticles([this.checkedArticles[0]]);
          extensionArticles = this.collectArticles(this.checkedArticles.slice(1));
        }
        route.push('/new');
        break;
      default:
        console.error('Unknown mode:', mode);
    }

    this.router.navigate(route, { queryParams: { itemArticles, extensionArticles } });
  }

}
