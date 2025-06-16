import { Article } from './Order';
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
  is_deinventoried: boolean,
  created_at: string,
  tags: Tag[],
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

export function inventoryItemFromArticle(article: Article): InventoryItem {
  return {
    id: article.inventories_id,
    description: article.description,
    serial_number: article.inventories_serial_number,
    price: article.price,
    location: article.location,
    cost_center: '', // Assuming cost center is not available in Article
    company: article.company,
    orderer: article.orderer,
    is_deinventoried: false, // Assuming this is not available in Article
    created_at: new Date().toISOString(), // Assuming current date as created_at
    tags: [] // Assuming no tags are available in Article
  } as InventoryItem;
}
