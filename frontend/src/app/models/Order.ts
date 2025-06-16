import { Article } from "./Article";

export interface Order {
  id: number,
  description: string,
  price: number,
  company: string,
  order_date: string,
  orderer: string,
  articles: Article[],
}

export interface ArticleId {
  orderId: number;
  articleId: number;
}

/**
 * Fixes the case where a single article string is provided as a query parameter.
 * If the string contains only one comma, it is treated as a single article.
 * This is a workaround for the case where only one article is provided as a query parameter.
 *
 * @param articleSttrings - An array of article strings, which may contain a single article string.
 * @returns An array of article strings. If the input contains multiple articles, it returns the original array.
 */
export function fixSingleArticleString(articleSttrings: string[]): string[] {
  const singleArticleString = articleSttrings.join('');
  if (([...singleArticleString.matchAll(/,/g)]).length === 1) {
    return [singleArticleString];
  }
  return articleSttrings;
}
