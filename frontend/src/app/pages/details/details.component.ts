import { CommonModule } from '@angular/common';
import { afterNextRender, Component, input, QueryList, ViewChildren } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule, MatExpansionPanel } from '@angular/material/expansion';
import { Router, RouterModule } from '@angular/router';
import { localizePrice } from '../../app.component';
import { DynamicListComponent } from "../../components/dynamic-list/dynamic-list.component";
import { Change } from '../../models/change';
import { Comment } from '../../models/comment';
import { Extension, extensionDisplayNames, extensionLocalizePrice } from '../../models/extension';
import { InventoryItem, inventoryItemDisplayNames } from '../../models/inventory-item';
import { getTagColor, Tag } from '../../models/tag';

/**
 * DetailsComponent
 *
 * This component displays detailed information about a specific inventory item, including its main attributes,
 * related extensions, comments, tags, and change history. The information is organized into expandable panels
 * using Angular Material Expansion Panels. Each panel presents its data using dynamic lists or chip sets.
 *
 * ## Inputs
 * - `inventoryItem`: The main inventory item to display (type: InventoryItem, required).
 * - `extensions`: Array of extension objects related to the inventory item (type: Extension[]).
 * - `comments`: Array of comments related to the inventory item (type: Comment[]).
 * - `changes`: Array of change history entries (type: Change[]) for the inventory item.
 *
 * ## Features
 * - Displays item attributes in a two-column layout.
 * - Shows related extensions, comments, and change history in expandable panels with dynamic column headers.
 * - Displays an items tags as chips.
 * - Automatically opens all expansion panels after rendering.
 * - Handles empty states for each panel.
 * - Transforms change history entries to merge table and column names for display.
 *
 * @example
 * <app-details
 *   [inventoryItem]="item"
 *   [extensions]="itemExtensions"
 *   [comments]="itemComments"
 *   [changes]="itemChanges">
 * </app-details>
 */
@Component({
  selector: 'app-details',
  imports: [
    MatDividerModule,
    MatExpansionModule,
    MatChipsModule,
    DynamicListComponent,
    RouterModule,
    MatButtonModule,
    CommonModule
  ],
  templateUrl: './details.component.html',
  styleUrl: './details.component.css'
})
export class DetailsComponent {
  panelIdNameMap = new Map<string, string>([
    ['extensions', 'Erweiterungen'],
    ['comments', 'Kommentare'],
    ['changes', 'Historie']
  ]);

  @ViewChildren('Panels') panels!: QueryList<MatExpansionPanel>;

  inventoryItem = input.required<InventoryItem>();
  extensions = input([], { transform: extensionLocalizePrice });
  comments = input<Comment[]>([]);
  tags: Tag[] = [];
  // The transform merges table and column names for change history entries to display them in a single column
  changes = input([], { transform: mergeChangeLocation });

  inventoryItemInternal!: Map<string, string>;

  panelContent = new Map<string, any>([
    ['extensions', this.extensions],
    ['comments', this.comments],
    ['changes', this.changes]
  ]);


  // Defines the column headers for the different panels
  // The keys are the internal names of the columns, the values are the display names
  inventoryItemColumns = inventoryItemDisplayNames;

  extensionColumns = new Map(extensionDisplayNames);

  commentsColumns = new Map<string, string>([
    ['description', 'Kommentar'],
    ['author', 'Hinzugefügt von'],
    ['createdAt', 'Hinzugefügt am']
  ]);

  changesColumns = new Map<string, string>([
    ['changedAt', 'Geändert am'],
    ['change', 'Änderung'],
    ['oldValue', 'Alter Wert'],
    ['newValue', 'Neuer Wert'],
    ['changedBy', 'Geändert von']
  ]);

  panelColumnMaps = new Map<string, Map<string, string>>([
    ['extensions', this.extensionColumns],
    ['comments', this.commentsColumns],
    ['changes', this.changesColumns]
  ]);

  constructor(private readonly router: Router) {
    this.extensionColumns.set('actions', '');
    afterNextRender(() => {
      for (const panel of this.panels) {
        if (!panel.id.includes('3')) {
          panel.open();
        }
      }
    });
  }

  /**
 * Lifecycle hook that is called when any data-bound property of a directive changes.
 * Initializes the internal representation of the inventory item and its tags for display.
 * If no inventory item is provided, sets default values for display.
 */
  ngOnChanges() {
    if (this.inventoryItem() && JSON.stringify(this.inventoryItem()) !== '{}') {
      this.inventoryItemInternal = new Map<string, string>();

      for (const key of this.inventoryItemColumns.keys()) {
        const value = this.inventoryItem()[key as keyof InventoryItem];
        this.inventoryItemInternal.set(key, value ? value.toString() : '');
      }
      this.inventoryItemInternal.set('price', localizePrice(this.inventoryItemInternal.get('price')!));

      this.tags = this.inventoryItem().tags ?? [];
    } else {
      this.inventoryItemInternal = new Map<string, string>();
      for (let id of this.inventoryItemColumns.keys()) {
        this.inventoryItemInternal.set(id, id.toLocaleUpperCase());
      }
    }
  }

  onClickExtension(extension: object): void {
    const ext = extension as Extension;
    this.router.navigate(['/edit', this.inventoryItem().id, 'extension', ext.id]);
  }

  getTagColor(tagName: string): string {
    return getTagColor(tagName);
  }

}

// Defines the table and column display names for the change history entries
const changesTableNames = new Map<string, string>([
  ['inventory_items', 'Hauptartikel'],
  ['extensions', 'Erweiterung']
]);
const changesColumnNames = new Map<string, string>([
  ['location', 'Standort/Nutzer:in'],
  ['price', 'Preis'],
  ['company', 'Firma'],
  ['description', 'Beschreibung'],
  ['serialNumber', 'Seriennummer'],
  ['orderer', 'Besteller:in'],
  ['costCenter', 'Kostenstelle'],
  ['createdAt', 'Erstellungsdatum'],
]);

/**
 * Helper function to merge table and column names for change history entries.
 * Used to transform raw change history data for display in the change history panel.
 * @param {Change[]} rawChanges - The raw change history entries.
 * @returns {ChangeInternal[]} The transformed change history entries.
 */
function mergeChangeLocation(rawChanges: Change[]): ChangeInternal[] {
  let changes = rawChanges.map((change: Change) => {
    // const changedTableDisplayName = changesTableNames.get(change.changedTable) ?? change.changedTable; Disabled as only the inventory_item table is tracked for now
    const changedColumnDisplayName = changesColumnNames.get(change.attributeChanged) ?? change.attributeChanged;

    const internalChange: ChangeInternal = {
      changedAt: change.createdAt,
      changedBy: change.changedBy,
      change: changedColumnDisplayName, // `${changedTableDisplayName} - ${changedColumnDisplayName}`,
      oldValue: change.valueFrom,
      newValue: change.valueTo
    };

    if (change.attributeChanged === 'price') {
      internalChange.oldValue = change.valueFrom ? localizePrice(change.valueFrom) : '';
      internalChange.newValue = change.valueTo ? localizePrice(change.valueTo) : '';
    }
    return internalChange;

  })
  return changes;
}

interface ChangeInternal {
  changedAt: string;
  changedBy: string;
  change: string;
  oldValue: string;
  newValue: string;
}
