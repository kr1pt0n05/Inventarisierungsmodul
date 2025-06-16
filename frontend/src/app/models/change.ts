export interface Change {
  id: number;
  createdAt: string;
  changedBy: string;
  changedTable: string;
  changedColumn: string;
  attributeChanged: string;
  valueFrom: string;
  valueTo: string;
}
