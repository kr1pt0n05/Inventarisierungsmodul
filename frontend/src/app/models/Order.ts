export interface Order {
  id: number,
  description: string,
  price: number,
  company: string,
  order_date: string,
  orderer: string,
  articles: Article[],
}

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
  checked: boolean, // Needed for Orders page
}

export interface ArticleId {
  orderId: number;
  articleId: number;
}
