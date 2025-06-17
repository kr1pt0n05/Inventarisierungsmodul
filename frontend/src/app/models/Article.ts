import { Tag } from "./tag";

export interface Article {
  description: string,
  price: number,
  company: string,
  location: string,
  orderer: string,
  article_id: number,
  inventories_id: number,
  inventories_serial_number: string,
  is_extension: boolean,
  is_inventoried: boolean,
  tags: Tag[],
  checked: boolean, // Needed for Orders page
}
