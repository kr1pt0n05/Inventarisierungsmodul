import { Tag } from './tag';

export interface Tags {
  content: Tag[];
  totalPages: number;
  totalElements: number;
  size: number;
  empty: boolean;
}
