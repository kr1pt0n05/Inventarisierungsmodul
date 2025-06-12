import {Article} from "./Article";

export interface Order {
  id: number,
  description: string,
  price: number,
  company: string,
  order_date: string,
  orderer: string,
  articles: Article[],
}
