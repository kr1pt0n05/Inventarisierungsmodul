import {InventoryItem} from './inventory-item';

export interface Inventories {
  content: InventoryItem[];
  totalPages: number;
  totalElements: number;
  size: number;
  empty: boolean;
}
