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
import {ServerTableDataSourceService} from '../../services/server-table-data-source.service';
import {FormControl, ReactiveFormsModule} from '@angular/forms';



/**
 * InventoryListComponent
 *
 * This Angular component manages the display of inventory items in a paginated and sortable table.
 * It integrates with the `ServerTableDataSourceService` to provide dynamic pagination, sorting, and searching functionality.
 * The table displays a list of inventory items with details such as ID, description, company, price, serial number, location, and orderer.
 *
 * Features:
 * - Displays inventory items in a Material Table.
 * - Supports pagination and sorting.
 * - Allows searching by item description using a search bar.
 *
 * Properties:
 * - displayedColumns: List of column identifiers to be displayed in the table.
 * - inventoryItems: Instance of `ServerTableDataSourceService` that provides the data source for the table.
 * - paginator: Reference to the paginator used for handling pagination.
 * - sort: Reference to the MatSort used for sorting columns.
 * - searchText: FormControl bound to the search input for filtering inventory items.
 *
 * Methods:
 * - ngAfterViewInit(): Initializes the table by setting up paginator, sorting, and search functionality.
 *
 * Usage:
 * Place `<app-inventory-list>` in your template. The component will automatically integrate with the `ServerTableDataSourceService`
 * for fetching and displaying inventory items.
 *
 * Dependencies:
 * - Angular Material modules for table, paginator, sort, and input.
 * - ReactiveFormsModule for managing form controls.
 * - `ServerTableDataSourceService` for data handling.
 */
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
    ReactiveFormsModule,
  ],
  templateUrl: './inventory-list.component.html',
  styleUrl: './inventory-list.component.css'
})
export class InventoryListComponent implements AfterViewInit{

  /**
   * List of column identifiers that should be displayed in the table.
   * These correspond to the columns of the inventory items being displayed.
   */
  displayedColumns = ['id', 'description', 'company', 'price', 'date', 'serialNumber', 'location', 'orderer'];

  /**
   * The data source service for managing inventory items in the table.
   * This service handles pagination, sorting, filtering and searching of data.
   */
  inventoryItems = inject(ServerTableDataSourceService);

  /**
   * Reference to the MatPaginator component used for handling pagination.
   * This is set up in the `ngAfterViewInit()` lifecycle hook.
   */
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  /**
   * Reference to the MatSort component used for sorting columns.
   * This is set up in the `ngAfterViewInit()` lifecycle hook.
   */
  @ViewChild(MatSort) sort!: MatSort;

  /**
   * Form control bound to the search input field.
   * This control is used to filter the displayed inventory items based on search text.
   */
  searchText = new FormControl('');

  /**
   * Initializes the component and sets up the paginator, sorter, and search bar.
   * This method is called after the view is initialized (ngAfterViewInit).
   */
  ngAfterViewInit() {
    // Set paginator and sorter for the data source
    this.inventoryItems.paginator = this.paginator;
    this.paginator._intl.itemsPerPageLabel = "Artikel pro Seite:"; // Set paginator label
    this.inventoryItems.sorter = this.sort;

    // Bind the search bar to the data source
    this.inventoryItems.searchbar = this.searchText;
  }

}
