export interface InventoryItemChange {
  date: string;
  inventoryNumber: number;
  changedBy: string;
  changedTable: string;
  changedColumn: string;
  oldValue: string;
  newValue: string;
}
