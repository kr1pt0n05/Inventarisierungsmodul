import {AfterViewInit, Component, inject, ViewChild} from '@angular/core';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef,
  MatTable,
} from '@angular/material/table';
import {MatPaginator, MatPaginatorModule} from '@angular/material/paginator';
import {MatSort, MatSortModule} from '@angular/material/sort';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {DatePipe} from '@angular/common';
import {ServerTableDataSourceService} from '../../services/server-table-data-source.service';

@Component({
  selector: 'app-inventory-list',
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
    MatPaginator,
    MatPaginatorModule,
    MatSortModule,
    MatInput,
    MatFormField,
    MatLabel,
  ],
  templateUrl: './inventory-list.component.html',
  styleUrl: './inventory-list.component.css'
})
export class InventoryListComponent implements AfterViewInit{

  displayedColumns = ['id', 'description', 'company', 'price', 'date', 'serialNumber', 'location', 'orderer'];
  inventoryItems = inject(ServerTableDataSourceService);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngAfterViewInit() {
    this.inventoryItems.paginator = this.paginator;
    this.paginator._intl.itemsPerPageLabel = "Artikel pro Seite:";
    this.inventoryItems.sorter = this.sort;
  }

}
