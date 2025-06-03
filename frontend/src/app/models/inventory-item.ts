import { Tag } from './tag';

export interface InventoryItem {
  id: number,
  description: string,
  serialNumber: string,
  price: number,
  location: string,
  costCenter: string,
  company: string,
  orderer: string,
  isDeinventoried: boolean,
  createdAt: string,
  tags: Tag[],
  [key: string]: any
}
