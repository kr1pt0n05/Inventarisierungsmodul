import { Component, input, OnInit, signal } from '@angular/core';
import { InventoryItemEditorComponent } from '../../components/inventory-item-editor/inventory-item-editor.component';
import { Article } from '../../models/Article';
import { InventoryItem } from '../../models/inventory-item';
import { InventoriesService } from '../../services/inventories.service';

@Component({
  selector: 'app-orderization',
  imports: [
    InventoryItemEditorComponent
  ],
  templateUrl: './orderization.component.html',
  styleUrl: './orderization.component.css'
})
export class OrderizationComponent implements OnInit {


  constructor(private readonly inventoriesService: InventoriesService,) {
  }

  /**
   * Input signal for the inventory item to be edited.
   */
  article = input<Article | undefined>(undefined);

  /**
   * Signal holding a mutable copy of the inventory item for editing.
   */
  editableArticle = signal<InventoryItem>({} as InventoryItem);


  /**
   * Initializes the editable inventory item and loads comments if an item is present.
   */
  ngOnInit() {
    if (this.article()) this.editableArticle.set({
      id: this.article()?.article_id ?? 0,
      description: this.article()?.description ?? "",
      serial_number: this.article()?.inventories_serial_number ?? "",
      price: this.article()?.price ?? 0,
      location: this.article()?.location ?? "",
      company: this.article()?.company ?? "",
      orderer: this.article()?.orderer ?? "",
      cost_center: '',
      is_deinventoried: false,
      created_at: ''
    });
  }


  /**
   * Saves changes to the inventory item and handles comment changes.
   * Emits the onInventorization event after saving.
   */
  saveInventorization() {
    this.inventoriesService.addInventoryItem(this.editableArticle()).subscribe({
      next: (newItem) => {
        console.log('New inventory item created successfully:', newItem);
      },
      error: (error) => {
        console.error('Error creating new inventory item:', error);
      }
    })
  }


  // navigate back to Orders
  navigateBack() {
    window.history.back();
  }
}
