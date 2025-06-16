import { Article } from "./Order";

export interface Extension {
  id?: number,
  cost_center: string,
  description: string,
  company: string,
  price: number,
  created_at: string,
  serial_number: string,
  orderer: string,
  [key: string]: any;
}

export const extensionDisplayNames: Map<string, string> = new Map([
  ['description', 'Erweiterungstyp'],
  ['company', 'Bestellt bei'],
  ['price', 'Preis in €'],
  ['cost_center', 'Kostenstelle'],
  ['serial_number', 'Seriennummer'],
  ['orderer', 'Hinzugefügt von'],
  ['created_at', 'Hinzugefügt am']
]);

export function extensionItemFromArticle(article: Article): Extension {
  return {
    description: article.description,
    price: article.price,
    cost_center: '', // Assuming cost center is not available in Article
    company: article.company,
    orderer: article.orderer,
    created_at: new Date().toISOString(), // Assuming current date as created_at
    serial_number: article.inventories_serial_number,
  } as Extension;
}
