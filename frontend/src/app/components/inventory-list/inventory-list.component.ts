import {AfterViewInit, Component, ViewChild} from '@angular/core';
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

  displayedColumns: string[] = ['costCenter', 'inventoryNumber', 'productDescription', 'company', 'price', 'date', 'serialNumber', 'location', 'orderer'];

  inventoryItems = new MatTableDataSource<InventoryItem>([
    {costCenter: 754880850, inventoryNumber: 27587, productDescription: "HP Color Laserjet CP2025DN", company: "redcoon", price: 432.00, date: "04.04.11", serialNumber: "", location: "IT-1", orderer: "Max"},
    {costCenter: 754880850, inventoryNumber: 27588, productDescription: "HP Color Laserjet CP2025DN", company: "redcoon", price: 432.00, date: "04.04.11", serialNumber: "asdasdasdasd", location: "IT-Fachschaft", orderer: "Max"},
    {costCenter: 754880850, inventoryNumber: 27589, productDescription: "HP Color Laserjet CP2025DN", company: "redcoon", price: 432.00, date: "04.04.11", serialNumber: "", location: "IT-2", orderer: "Max"},
    {costCenter: 754880850, inventoryNumber: 27590, productDescription: "HP Color Laserjet CP2025DN", company: "redcoon", price: 432.00, date: "04.04.11", serialNumber: "", location: "IT-3", orderer: "Max"},
    {costCenter: 754880850, inventoryNumber: 27591, productDescription: "HP Color Laserjet CP2025DN", company: "redcoon", price: 432.00, date: "04.04.11", serialNumber: "asdasdasdasd", location: "IT-4", orderer: "Max"},
    {costCenter: 754880850, inventoryNumber: 27592, productDescription: "HP Color Laserjet CP2025DN", company: "redcoon", price: 432.00, date: "04.04.11", serialNumber: "", location: "IT-5", orderer: "Max"},
    {costCenter: 754880850, inventoryNumber: 27593, productDescription: "HP Color Laserjet CP2025DN", company: "redcoon", price: 432.00, date: "04.04.11", serialNumber: "", location: "IT-6", orderer: "Max"},
    {costCenter: 754880850, inventoryNumber: 27594, productDescription: "HP Color Laserjet CP2025DN", company: "redcoon", price: 432.00, date: "04.04.11", serialNumber: "asdasdasdasd", location: "IT-7", orderer: "Max"},
    {costCenter: 754880850, inventoryNumber: 27595, productDescription: "HP Color Laserjet CP2025DN", company: "redcoon", price: 432.00, date: "04.04.11", serialNumber: "", location: "IT-8", orderer: "Max"},
    {costCenter: 754880850, inventoryNumber: 27596, productDescription: "HP Color Laserjet CP2025DN", company: "redcoon", price: 432.00, date: "04.04.11", serialNumber: "", location: "IT-9", orderer: "Max"},
    {costCenter: 754880850, inventoryNumber: 27597, productDescription: "HP Color Laserjet CP2025DN", company: "redcoon", price: 432.00, date: "04.04.11", serialNumber: "asdasdasdasd", location: "IT-10", orderer: "Max"},
    {costCenter: 754880850, inventoryNumber: 27598, productDescription: "HP Color Laserjet CP2025DN", company: "redcoon", price: 432.00, date: "04.04.11", serialNumber: "", location: "IT-11", orderer: "Max"},
    {costCenter: 754880850, inventoryNumber: 27599, productDescription: "HP Color Laserjet CP2025DN", company: "redcoon", price: 432.00, date: "04.04.11", serialNumber: "", location: "IT-12", orderer: "Max"},
    {costCenter: 754880850, inventoryNumber: 27600, productDescription: "HP Color Laserjet CP2025DN", company: "redcoon", price: 432.00, date: "04.04.11", serialNumber: "asdasdasdasd", location: "IT-13", orderer: "Max"},
    {costCenter: 754880850, inventoryNumber: 26010, productDescription: "HP Color Laserjet CP2025DN", company: "redcoon", price: 432.00, date: "04.04.11", serialNumber: "", location: "IT-14", orderer: "Max"},
    {costCenter: 754880850, inventoryNumber: 27602, productDescription: "HP Color Laserjet CP2025DN", company: "redcoon", price: 432.00, date: "04.04.11", serialNumber: "", location: "IT-15", orderer: "Max"},
    {costCenter: 754880850, inventoryNumber: 27603, productDescription: "HP Color Laserjet CP2025DN", company: "redcoon", price: 432.00, date: "04.04.11", serialNumber: "asdasdasdasd", location: "IT-16", orderer: "Max"},
    {costCenter: 754880850, inventoryNumber: 27604, productDescription: "HP Color Laserjet CP2025DN", company: "redcoon", price: 432.00, date: "04.04.11", serialNumber: "", location: "IT-17", orderer: "Max"},
  ]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngAfterViewInit() {
    this.inventoryItems.paginator = this.paginator;
    this.inventoryItems.sort = this.sort;
  }

  filterInventoryItems(event: Event){
    const filterValue = (event.target as HTMLInputElement).value;
    this.inventoryItems.filter = filterValue.trim().toLowerCase();
  }

}
