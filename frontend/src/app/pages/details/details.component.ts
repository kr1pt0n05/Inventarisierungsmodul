import { NgForOf, NgIf } from '@angular/common';
import { afterNextRender, Component, input, QueryList, ViewChildren } from '@angular/core';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule, MatExpansionPanel } from '@angular/material/expansion';
import { DynamicListComponent } from "../../components/dynamic-list/dynamic-list.component";
import { InventoryExtension } from '../../models/inventory-extension';
import { InventoryItem } from '../../models/inventory-item';
import { InventoryItemChange } from '../../models/inventory-item-change';
import { InventoryItemNotes } from '../../models/inventory-item-notes';
import { Tag } from '../../models/tag';

/**
 * Component for displaying detailed information about an inventory item.
 *
 * This component organizes and displays the main attributes of an inventory item as well as its related
 * extensions, notes, tags, and change history. The information is grouped into expandable panels using
 * Angular Material Expansion Panels. Each panel uses a dynamic list or chip set to present its data.
 *
 * ## Inputs
 * - `inventoryItemInput`: The main inventory item to display (type: InventoryItem).
 * - `extensions`: Array of extension objects related to the inventory item (type: InventoryExtension[]).
 * - `notes`: Array of notes related to the inventory item (type: InventoryItemNotes[]).
 * - `changes`: Array of change history entries (type: InventoryItemChange[]) for the inventory item.
 *
 * ## Features
 * - Displays item attributes in a two-column layout.
 * - Shows related extensions, notes, and change history in expandable panels with dynamic column headers.
 * - Displays an items tags as chips.
 * - Automatically opens all expansion panels after rendering.
 * - Handles empty states for each panel.
 *
 * @example
 * <app-details
 *   [inventoryItemInput]="item"
 *   [extensions]="itemExtensions"
 *   [notes]="itemNotes"
 *   [changes]="itemChanges">
 * </app-details>
 */
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
  panelIdNameMap = new Map<string, string>([
    ['extensions', 'Erweiterungen'],
    ['notes', 'Notizen'],
    ['tags', 'Tags'],
    ['changes', 'Historie']
  ]);

  @ViewChildren('Panels') panels!: QueryList<MatExpansionPanel>;

  inventoryItem = input.required<InventoryItem>();
  extensions = input<InventoryExtension[]>([]);
  notes = input<InventoryItemNotes[]>([]);
  tags: Tag[] = [];
  // The transform merges table and column names for change history entries to display them in a single column
  changes = input([], { transform: mergeChangeLocation });

  inventoryItemInternal!: Map<string, string>;

  panelContent = new Map<string, any>([
    ['extensions', this.extensions],
    ['notes', this.notes],
    ['changes', this.changes]
  ]);


  // Defines the column headers for the different panels
  // The keys are the internal names of the columns, the values are the display names
  inventoryItemColumns = new Map<string, string>([
    ['costCenter', 'Kostenstelle'],
    ['id', 'Inventarnummer'],
    ['description', 'Geräte-/Softwaretyp'],
    ['company', 'Firma'],
    ['price', 'Preis'],
    ['date', 'Bestelldatum'],
    ['serialNumber', 'Seriennummer'],
    ['location', 'Standort/Nutzer:in'],
    ['orderer', 'Bestellt von']
  ]);

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

  panelColumnMaps = new Map<string, Map<string, string>>([
    ['extensions', this.extensionColumns],
    ['notes', this.notesColumns],
    ['changes', this.changesColumns]
  ]);

  constructor() {
    afterNextRender(() => {
      this.panels.map((panel: MatExpansionPanel) => {
        panel.open();
      })
    });
  }

  ngOnChanges() {
    if (this.inventoryItem() && JSON.stringify(this.inventoryItem()) !== '{}') {
      this.inventoryItemInternal = new Map<string, string>([
        ['description', this.inventoryItem().description],
        ['costCenter', this.inventoryItem().costCenter.toString()],
        ['id', this.inventoryItem().id.toString()],
        ['company', this.inventoryItem().company],
        ['price', this.inventoryItem().price.toString()],
        ['date', this.inventoryItem().createdAt],
        ['serialNumber', this.inventoryItem().serialNumber],
        ['location', this.inventoryItem().location],
        ['orderer', this.inventoryItem().orderer]
      ]);
      this.tags = this.inventoryItem().tags;
    } else {
      this.inventoryItemInternal = new Map<string, string>();
      for (let id of this.inventoryItemColumns.keys()) {
        this.inventoryItemInternal.set(id, id.toLocaleUpperCase());
      }
    }
  }

}

// Defines the table and column display names for the change history entries
const changesTableNames = new Map<string, string>([
  ['inventory_items', 'Hauptartikel'],
  ['extensions', 'Erweiterung']
]);
const changesColumnNames = new Map<string, string>([
  ['location', 'Standort/Nutzer:in'],
  ['price', 'Preis in €']
]);

/**
 * Helper function to merge table and column names for change history entries.
 * @param {InventoryItemChange[]} rawChanges - The raw change history entries.
 * @returns {any[]} The transformed change history entries.
 */
function mergeChangeLocation(rawChanges: InventoryItemChange[]): InventoryItemChangeInternal[] {
  let changes = rawChanges.map((change: InventoryItemChange) => {
    const changedTableDisplayName = changesTableNames.get(change.changedTable) ?? change.changedTable;
    const changedColumnDisplayName = changesColumnNames.get(change.changedColumn) ?? change.changedColumn;
    return {
      date: change.date,
      inventoryNumber: change.inventoryNumber,
      changedBy: change.changedBy,
      change: `${changedTableDisplayName} - ${changedColumnDisplayName}`,
      oldValue: change.oldValue,
      newValue: change.newValue
    };
  })
  return changes;
}

interface InventoryItemChangeInternal {
  date: string;
  inventoryNumber: number;
  changedBy: string;
  change: string;
  oldValue: string;
  newValue: string;
}
