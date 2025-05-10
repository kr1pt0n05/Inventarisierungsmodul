import { afterNextRender, Component, input, Input, signal, ViewChild, viewChildren, ViewChildren } from '@angular/core';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule, MatExpansionPanel } from '@angular/material/expansion';
import { InventoryItem } from '../../models/inventory-item';
import { InventoryExtension } from '../../models/inventory-extension';
import { InventoryItemNotes } from '../../models/inventory-item-notes';
import { InventoryItemChange } from '../../models/inventory-item-change';
import { NgForOf } from '@angular/common';

@Component({
  selector: 'app-details',
  imports: [
    MatDividerModule,
    MatExpansionModule,
    NgForOf
  ],
  templateUrl: './details.component.html',
  styleUrl: './details.component.css'
})
export class DetailsComponent {
  panelNames: string[] = ['Erweiterungen', 'Notizen', 'Tags', 'Historie'];

  panels = viewChildren<MatExpansionPanel>('Panels');

  inventoryItem = input.required<InventoryItem>();
  extensions = input.required<InventoryExtension[]>();
  notes = input.required<InventoryItemNotes[]>();
  tags = input.required<string[]>();
  changes = input.required<InventoryItemChange[]>();

  constructor() {
    afterNextRender(() => {
      for (let i = 0; i < this.panelNames.length - 1; i++) {
        this.panels()[i].open();
      }
    });
  }
}
