package com.hs_esslingen.insy.service;

import com.hs_esslingen.insy.model.Inventory;
import com.hs_esslingen.insy.repository.InventoryRepository;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class ExcelService {

    private final InventoryRepository inventoryRepository;

    public ResponseEntity<Resource> exportExcel() throws IOException {

        // Build the .xlsx file
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Inventar");

        // Fetch all the necessary data
        List<Inventory> inventoryList = inventoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        // First row font cell style
        String[] headings = {
                "Kostenstellen", "Inventarnummer", "Anzahl", "Beschreibung",
                "Firma", "Preis", "Erstellungsdatum", "Seriennummer",
                "Ort/Benutzer", "Besteller"
        };
        Row rowHeading = sheet.createRow(0);
        CellStyle rowHeadingStyle = wb.createCellStyle();
        Font rowHeadingFont = wb.createFont();
        rowHeadingFont.setBold(true);
        rowHeadingStyle.setAlignment(HorizontalAlignment.CENTER);
        rowHeadingStyle.setFont(rowHeadingFont);

        for (int i = 0; i < headings.length; i++) {
            Cell cell = rowHeading.createCell(i);
            cell.setCellValue(headings[i]);
            cell.setCellStyle(rowHeadingStyle);
        }

        // Deinventoried font style
        CellStyle deinventoriedStyle = wb.createCellStyle();
        Font deinventoriedFont = wb.createFont();
        deinventoriedFont.setColor(IndexedColors.RED.getIndex());
        deinventoriedFont.setStrikeout(true);
        deinventoriedStyle.setFont(deinventoriedFont);
        

        for (int i = 0; i < inventoryList.size(); i++) {
            Row row = sheet.createRow(i+1);
            Inventory inventory = inventoryList.get(i);

            row.createCell(0).setCellValue(inventory.getCostCenter() == null ? "" : inventory.getCostCenter().getDescription());
            row.createCell(1).setCellValue(inventory.getId());
            row.createCell(2).setCellValue(1);
            row.createCell(3).setCellValue(inventory.getDescription());
            row.createCell(4).setCellValue(inventory.getCompany() == null ? "" : inventory.getCompany().getName());
            row.createCell(5).setCellValue(inventory.getPrice().doubleValue());
            row.createCell(6).setCellValue(inventory.getCreatedAt());
            row.createCell(7).setCellValue(inventory.getSerialNumber());
            row.createCell(8).setCellValue(inventory.getLocation());
            row.createCell(9).setCellValue(inventory.getUser() == null ? "" : inventory.getUser().getName());

            if(inventory.getIsDeinventoried()){
                for(int j = 0; j < 10; j++){
                    row.getCell(j).setCellStyle(deinventoriedStyle);
                }
            }
        }

        // Write it to a byte array
        // Return it
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        wb.write(outputStream);
        Resource resource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .header("content-disposition", "attachment; filename=export.xls")
                .body(resource);
    }
}
