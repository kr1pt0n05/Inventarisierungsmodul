import {Company} from './company';
import {User} from './user';

export interface InventoryItem {
  // costCenter: number,
  id: number,
  user: string,
  description: string,
  company: string,
  price: number,
  createdAt: string,
  serialNumber: string,
  location: string,
  orderer: string,
}
