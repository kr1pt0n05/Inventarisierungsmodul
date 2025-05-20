export interface InventoryItem {
  // costCenter: number,
  id: number,
  description: string,
  company: string,
  price: number,
  date: string,
  serialNumber: string,
  location: string,
  orderer: string,
  [key: string]: any
}
