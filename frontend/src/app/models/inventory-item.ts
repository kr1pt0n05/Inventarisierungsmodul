import { Tag } from './tag';

export interface InventoryItem {
  id: number,
  description: string,
  serial_number: string,
  price: number,
  location: string,
  cost_center: string,
  company: string,
  orderer: string,
  isDeinventoried?: boolean,
  createdAt?: string,
  tags?: Tag[],
  is_deinventoried: boolean,
  created_at: string,
  [key: string]: any
}

export const inventoryItemDisplayNames: Map<string, string> = new Map([
  ['cost_center', 'Kostenstelle'],
  ['id', 'Inventarnummer'],
  ['description', 'Geräte-/Softwaretyp'],
  ['company', 'Firma'],
  ['price', 'Preis'],
  ['created_at', 'Inventarisiert am'],
  ['serial_number', 'Seriennummer'],
  ['location', 'Standort/Nutzer:in'],
  ['orderer', 'Bestellt von']
]);
