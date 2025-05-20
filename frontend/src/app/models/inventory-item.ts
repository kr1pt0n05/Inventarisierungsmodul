import {Company} from './company';
import {User} from './user';

export interface InventoryItem {
  // costCenter: number,
  id: number,
  user: User,
  description: string,
  company: Company,
  price: number,
  createdAt: string,
  serialNumber: string,
  location: string,
  orderer: string,
  [key: string]: any
}
