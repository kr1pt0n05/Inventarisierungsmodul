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

@Component({
  selector: 'app-details-list',
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
  templateUrl: './details-list.component.html',
  styleUrl: './details-list.component.css'
})
export class DetailsListComponent implements AfterViewInit{

  tableContent = input.required<object[]>();
  columns = input.required<Map<string, string>>();
  items!: MatTableDataSource<object>;

  @ViewChild(MatSort) sort!: MatSort;

  ngOnInit() {
    this.items = new MatTableDataSource(this.tableContent());
  }

  ngAfterViewInit() {
    this.items.sort = this.sort;
  }

  getColumnIds(): string[] {
    return Array.from(this.columns().keys());
  }

}
