import { afterNextRender, Component, input, QueryList, ViewChildren } from '@angular/core';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule, MatExpansionPanel } from '@angular/material/expansion';
import { InventoryItem } from '../../models/inventory-item';
import { InventoryExtension } from '../../models/inventory-extension';
import { InventoryItemNotes } from '../../models/inventory-item-notes';
import { InventoryItemChange } from '../../models/inventory-item-change';
import { NgForOf, NgIf } from '@angular/common';
import { DynamicListComponent } from "../../components/dynamic-list/dynamic-list.component";
import { MatChipsModule } from '@angular/material/chips';

@Component({
  selector: 'app-details',
  imports: [
    MatDividerModule,
    MatExpansionModule,
    NgForOf,
    NgIf,
    MatChipsModule,
    DynamicListComponent
],
  templateUrl: './details.component.html',
  styleUrl: './details.component.css'
})
export class DetailsComponent {
  panelNames: string[] = ['Erweiterungen', 'Notizen', 'Tags', 'Historie'];

  @ViewChildren('Panels') panels!: QueryList<MatExpansionPanel>;

  inventoryItem = input<InventoryItem>();
  extensions = input<InventoryExtension[]>([]);
  notes = input<InventoryItemNotes[]>([
    { note: 'test',
      date: '12134',
      author: 'dg' }
  ]);
  tags = input<string[]>();
  changes = input([], { transform: mergeChangeLocation });

  panelContent = new Map<string, any>([
    ['Erweiterungen', this.extensions],
    ['Notizen', this.notes],
    ['Historie', this.changes]
  ]);


  // Defines the column headers for the different panels
  // The keys of the maps are the keys of the objects in the arrays
  extensionColumns = new Map<string, string>([
    ['productDescription', 'Erweiterungstyp'],
    ['company', 'Bestellt bei'],
    ['price', 'Preis in €'],
    ['costCenter', 'Kostenstelle'],
    ['serialNumber', 'Seriennummer'],
    ['orderer', 'Hinzugefügt von'],
    ['date', 'Hinzugefügt am']
  ]);
  notesColumns = new Map<string, string>([
    ['note', 'Notiz'],
    ['author', 'Hinzugefügt von'],
    ['date', 'Hinzugefügt am']
  ]);
  changesColumns = new Map<string, string>([
    ['date', 'Geändert am'],
    ['change', 'Änderung'],
    ['oldValue', 'Alter Wert'],
    ['newValue', 'Neuer Wert'],
    ['changedBy', 'Geändert von']
  ]);

  panelContentMap = new Map<string, Map<string, string>>([
    ['Erweiterungen', this.extensionColumns],
    ['Notizen', this.notesColumns],
    ['Historie', this.changesColumns]
  ]);

  constructor() {
    afterNextRender(() => {
      this.panels.map((panel: MatExpansionPanel) => {
        panel.open();
      })
    });
  }


}

function mergeChangeLocation(rawChanges: InventoryItemChange[]) {
  let changes = rawChanges.map((change: InventoryItemChange) => {
    return {
      date: change.date,
      inventoryNumber: change.inventoryNumber,
      changedBy: change.changedBy,
      change: change.changedTable + ' - ' + change.changedColumn,
      oldValue: change.oldValue,
      newValue: change.newValue
    };
  })
  return changes;
}