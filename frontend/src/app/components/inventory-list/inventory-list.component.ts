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
import {MatPaginator, MatPaginatorModule} from '@angular/material/paginator';
import {InventoryItem} from '../../models/inventory-item';
import {MatSort, MatSortModule} from '@angular/material/sort';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {InventoriesService} from '../../services/inventories.service';
import {DatePipe} from '@angular/common';

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

  constructor(private inventoryService: InventoriesService) {
    this.inventoryService = inventoryService;
  }

  displayedColumns = ['id', 'description', 'company', 'price', 'date', 'serialNumber', 'location', 'orderer'];
  inventoryItems = new MatTableDataSource<InventoryItem>();

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngOnInit() {
    this.inventoryService.getInventories().subscribe((inventories) =>{
      this.inventoryItems.data = inventories.content;
      console.log(inventories.content);
    })
  }

  ngAfterViewInit() {
    this.inventoryItems.paginator = this.paginator;
    this.inventoryItems.sort = this.sort;
  }

  filterInventoryItems(event: Event){
    const filterValue = (event.target as HTMLInputElement).value;
    this.inventoryItems.filter = filterValue.trim().toLowerCase();
  }

}
