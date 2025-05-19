import {AfterViewInit, Component, input, ViewChild} from '@angular/core';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef,
  MatTable, MatTableDataSource,
} from '@angular/material/table';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatSort, MatSortModule} from '@angular/material/sort';
import { NgForOf } from '@angular/common';

/**
 * A generic Angular component for displaying a dynamic list
 * using an Angular Material Table. The component expects the list data
 * as an array of objects and a map whose keys correspond to the attributes
 * of the data objects and whose values are used as the column display names.
 *
 * @example
 * <app-dynamic-list
 *   [tableContent]="[{name: 'Max', age: 25}, {name: 'Anna', age: 30}]"
 *   [columns]="new Map([['name', 'Name'], ['age', 'Age']])">
 * </app-dynamic-list>
 *
 * @input tableContent - Array of objects representing the table's row data.
 * @input columns - Map whose keys are the attribute names of the objects and whose values are the column display names.
 *
 * The component supports sorting via Angular Material.
 */
@Component({
  selector: 'app-dynamic-list',
  imports: [
    MatTable,
    MatColumnDef,
    MatHeaderCell,
    MatCell,
    MatHeaderCellDef,
    MatCellDef,
    MatHeaderRow,
    MatRow,
    MatRowDef,
    MatHeaderRowDef,
    MatPaginatorModule,
    MatSortModule,
    NgForOf,
  ],
  templateUrl: './dynamic-list.component.html',
  styleUrl: './dynamic-list.component.css'
})
export class DynamicListComponent implements AfterViewInit{

  // The list data as an array of objects.
  tableContent = input.required<object[]>();

  // Map with attribute names as keys and column display names as values.
  columns = input.required<Map<string, string>>();

  items!: MatTableDataSource<object>;
  @ViewChild(MatSort) sort!: MatSort;

  ngOnChanges() {
    this.items = new MatTableDataSource(this.tableContent());
  }

  ngAfterViewInit() {
    this.items.sort = this.sort;
  }

  /**
   * Returns the attribute names (column IDs) as an array.
   * @returns {string[]} Array of column IDs
   */
  getColumnIds(): string[] {
    return Array.from(this.columns().keys());
  }

}
