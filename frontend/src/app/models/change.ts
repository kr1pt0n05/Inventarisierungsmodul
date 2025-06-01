export interface Change {
  id: number;
  changedAt: string;
  changedBy: string;
  changedTable: string;
  changedColumn: string;
  oldValue: string;
  newValue: string;
}
