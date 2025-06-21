package com.hs_esslingen.insy.service;

import com.hs_esslingen.insy.dto.InventoryExcel;
import com.hs_esslingen.insy.model.Comment;
import com.hs_esslingen.insy.model.Company;
import com.hs_esslingen.insy.model.CostCenter;
import com.hs_esslingen.insy.model.Inventory;
import com.hs_esslingen.insy.model.User;
import com.hs_esslingen.insy.repository.*;
import com.hs_esslingen.insy.utils.StringParser;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class ExcelService {

    private final InventoryRepository inventoryRepository;
    private final CostCenterRepository costCentersRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final CommentRepository commentRepository;

    // Offset to start writing data from the specified row index
    // The first 2 Excel rows are left empty for compatibility purposes
    private final int STARTING_ROW_OFFSET = 1;

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

        // set columns min width
        sheet.setColumnWidth(0, 256 * 25); // costCenter
        sheet.setColumnWidth(1, 256 * 15); // InventoryNumber
        sheet.setColumnWidth(2, 256 * 8);  // amount
        sheet.setColumnWidth(3, 256 * 30);  // description
        sheet.setColumnWidth(4, 256 * 30);  // company
        sheet.setColumnWidth(5, 256 * 10);  // price
        sheet.setColumnWidth(6, 256 * 12);  // createdAt
        sheet.setColumnWidth(7, 256 * 20);  // serialNumber
        sheet.setColumnWidth(8, 256 * 20);  // location/user
        sheet.setColumnWidth(9, 256 * 20);  // orderer


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

        // Date cell style
        CellStyle dateCellStyle = wb.createCellStyle();
        dateCellStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("dd.MM.yyyy"));
        
        // Insert comments
        for (int i = 0; i < inventoryList.size(); i++) {
            Row row = sheet.createRow(i+ 1 + STARTING_ROW_OFFSET); // 1 offset for headings, STARTING_ROW_OFFSET for compatability with old excel
            Inventory inventory = inventoryList.get(i);
            List<Comment> comments = inventory.getComments();

            row.createCell(0).setCellValue(inventory.getCostCenter() == null ? "" : inventory.getCostCenter().getDescription());
            row.createCell(1).setCellValue(inventory.getId());
            row.createCell(2).setCellValue(1);
            row.createCell(3).setCellValue(inventory.getDescription());
            row.createCell(4).setCellValue(inventory.getCompany() == null ? "" : inventory.getCompany().getName());
            row.createCell(5).setCellValue(inventory.getPrice().doubleValue());


            Cell date = row.createCell(6);
            date.setCellStyle(dateCellStyle);
            date.setCellValue(Date.from(inventory.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant())); // Convert LocalDateTime to a Date to properly save it as date to excel

            row.createCell(7).setCellValue(inventory.getSerialNumber());
            row.createCell(8).setCellValue(inventory.getLocation());
            row.createCell(9).setCellValue(inventory.getUser() == null ? "" : inventory.getUser().getName());

            IntStream.range(10, 10 + comments.size()).forEach(j -> row.createCell(j).setCellValue(comments.get(j-10).getDescription()));

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


    public void importExcel(MultipartFile file) throws IOException {
        // Check file is not empty
        if(file.isEmpty()) throw new BadRequestException("File must not be empty!");

        // Check file extension. Must be .xls or .xlsx
        String fileName = file.getOriginalFilename();
        if(fileName == null || !(fileName.endsWith(".xls") || fileName.endsWith(".xlsx"))
        ) throw new BadRequestException("File must end with .xls or .xlsx!");

        // Creates appropriate Workbook (HSSFWorkbook for .xls and XSSFWorkbook for .xls)
        Workbook wb = WorkbookFactory.create(file.getInputStream());
        List<InventoryExcel> excelObjects = new ArrayList<>();


        // Parse Inventory Items of every sheet's row
        int numberOfSheets = wb.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = wb.getSheetAt(i);

            // Validate sheet name
            if(ExcelService.isYearBetweenXAndY(sheet.getSheetName(), 2000, 2100)){
                for (Row row : sheet) {

                    // Skip first row, since it contains headings
                    if (row.getCell(0) == null || row.getRowNum() == 0 || row.getRowNum() == 1) continue;

                    // Parse all values of excel files into one object
                    CellStyle style = row.getCell(1).getCellStyle();
                    Font font = wb.getFontAt(style.getFontIndex());


                    // Create Inventory Item, if inventoryNumber is present
                    if(ExcelService.getCellFormularValue(row.getCell(1)) != null){

                        // Create inventory items based on 'amount':
                        // - If amount == 1, create one item.
                        // - If amount > 1, duplicate the item 'amount' times and increase inventory number
                        Integer amount = ExcelService.getCellFormularValue(row.getCell(2));

                        for (int j = 0; j < amount; j++) {
                            InventoryExcel inv = new InventoryExcel();
                            inv.setCostCenter(ExcelService.getCellStringValue(row.getCell(0)));

                            int inventoryNumber = ExcelService.getCellFormularValue(row.getCell(1));
                            inv.setInventoryNumber(inventoryNumber + j);

                            inv.setDescription(ExcelService.getCellStringValue(row.getCell(3)));
                            inv.setCompany(ExcelService.getCellStringValue(row.getCell(4)) );
                            inv.setPrice(ExcelService.getCellDoubleValue(row.getCell(5)));
                            inv.setCreatedAt(ExcelService.getCellLocalDateValue(row.getCell(6)));
                            inv.setSerialNumber(ExcelService.getCellStringValue(row.getCell(7)) );
                            inv.setLocation(ExcelService.getCellStringValue(row.getCell(8)));
                            inv.setOrderer(ExcelService.getCellStringValue(row.getCell(9)));

                            inv.addComment(ExcelService.getCellStringValue(row.getCell(10)));
                            inv.addComment(ExcelService.getCellStringValue(row.getCell(11)));
                            inv.addComment(ExcelService.getCellStringValue(row.getCell(12)));
                            inv.addComment(ExcelService.getCellStringValue(row.getCell(13)));
                            inv.addComment(ExcelService.getCellStringValue(row.getCell(14)));
                            inv.addComment(ExcelService.getCellStringValue(row.getCell(15)));
                            inv.addComment(ExcelService.getCellStringValue(row.getCell(16)));


                            inv.setDeinventoried(font.getStrikeout());
                            excelObjects.add(inv);
                        }
                    }
                }
            }
        }

        // Clean data set
        excelObjects = excelObjects.stream()
                .filter(obj -> obj.getCostCenter() != null)
                .filter(obj -> !obj.getCostCenter().contains("*"))
                .filter(obj -> obj.getInventoryNumber() != null)
                .toList();

        // Only persist Excel Inventory Items to database, that do not already exist
        Set<Integer> existingInventoryNumbers = inventoryRepository.findInventoriesIdIn(excelObjects.stream().map(InventoryExcel::getInventoryNumber).toList());
        excelObjects = excelObjects.stream()
                .filter(obj -> !existingInventoryNumbers.contains(obj.getInventoryNumber()))
                .toList();

        // Parse each costCenter of Inventory Excel collection
        Set<String> excelCostCenters = excelObjects.stream()
                .map(InventoryExcel::getCostCenter)
                .filter(Objects::nonNull)
                .filter(costCenter -> !costCenter.contains("*"))
                .collect(Collectors.toSet());


        // Parse each User of Inventory Excel collection
        Set<String> excelUsers = excelObjects.stream()
                .map(InventoryExcel::getOrderer)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());


        // Parse each company of Inventory Excel collection
        Set<String> excelCompanies = excelObjects.stream()
                .map(InventoryExcel::getCompany)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());


        // Parse each comment of Inventory Excel collection
        Map<Integer, List<String>> excelComments = excelObjects.stream()
                .filter(obj -> obj.getComments() != null)  // Make sure comments are not null
                .collect(Collectors.groupingBy(
                        InventoryExcel::getInventoryNumber,  // Group by inventory number
                        Collectors.flatMapping(
                                obj -> obj.getComments().stream().filter(Objects::nonNull),  // Flatten the list of comments
                                Collectors.toList()  // Collect the comments into a list
                        )
                ));


        // 1. Get all <entity> of Excel Inventory Objects, remove duplicates & null ones
        // 2. Get all <entity>, that are in the excel sheet, off of the database if they exist there
        // 3. Subtract database <entity> from excel <entity> and persist excel <entity>
        // 4. Get newly persisted <entity> from the database
        // 5. Merge excel and newly persisted <entity> in one collection, so Inventory Item can use it for lookup


        // Get all costCenters of the excel Sheet that already exist in the database
        List<CostCenter> existingCostCenters = costCentersRepository.findByDescriptionIn(excelCostCenters);
        // Remove the already existing costCenters from the excel's costCenters
        excelCostCenters.removeAll(existingCostCenters.stream().map(CostCenter::getDescription).toList());
        // Persist the now remaining excel sheets costCenters (all costCenters that are new and do not already exist in the DB)
        // to the database;
        List<CostCenter> costCenters = costCentersRepository.saveAll(excelCostCenters.stream().map(CostCenter::new).toList());
        // finally merge the excel sheet costCenters (that were just persisted) and the database costCenters (that already existed)
        // and put them in a map for fast & easy access
        Map<String, CostCenter> costCentersMap = new HashMap<>();
        existingCostCenters.forEach(cc -> costCentersMap.put(cc.getDescription(), cc));
        costCenters.forEach(cc -> costCentersMap.put(cc.getDescription(), cc));


        List<User> existingUsers = userRepository.findByNameIn(excelUsers);
        excelUsers.removeAll(existingUsers.stream().map(User::getName).toList());
        List<User> users = userRepository.saveAll(excelUsers.stream().map(User::new).toList());
        Map<String, User> usersMap = new HashMap<>();
        existingUsers.forEach(u -> usersMap.put(u.getName(), u));
        users.forEach(u -> usersMap.put(u.getName(), u));


        List<Company> existingCompanies = companyRepository.findByNameIn(excelCompanies);
        excelCompanies.removeAll(existingCompanies.stream().map(Company::getName).toList());
        List<Company> companies = companyRepository.saveAll(excelCompanies.stream().map(Company::new).toList());
        Map<String, Company> companiesMap = new HashMap<>();
        existingCompanies.forEach(c -> companiesMap.put(c.getName(), c));
        companies.forEach(c -> companiesMap.put(c.getName(), c));


        // Put Inventory Items to a map, for easier access when creating comments
        Map<Integer, Inventory> inventory = new HashMap<>();
        excelObjects.forEach(obj -> {
            Inventory inv = new Inventory();
            inv.setId(obj.getInventoryNumber());
            inv.setCostCenter(costCentersMap.getOrDefault(obj.getCostCenter(), null));
            inv.setUser(usersMap.getOrDefault(obj.getOrderer(), null));
            inv.setCompany(companiesMap.getOrDefault(obj.getCompany(), null));
            inv.setDescription(obj.getDescription());
            inv.setSerialNumber(obj.getSerialNumber());
            inv.setIsDeinventoried(obj.isDeinventoried());
            inv.setPrice(BigDecimal.valueOf(obj.getPrice()));
            inv.setLocation(obj.getLocation());
            inv.setSearchText(StringParser.fullTextSearchString(inv));
            inventory.put(obj.getInventoryNumber(), inv);
        });
        inventoryRepository.saveAll(inventory.values());


        List<com.hs_esslingen.insy.model.Comment> comments = new ArrayList<>();
        excelComments.forEach((invNumber, commentsList) -> {
            Inventory inv = inventory.get(invNumber);
            commentsList.forEach(comment -> {
                com.hs_esslingen.insy.model.Comment c = new Comment();
                c.setInventories(inv);
                c.setDescription(comment);
                comments.add(c);

            });
        });
        commentRepository.saveAll(comments);

    }


    static String getCellStringValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            default:
                return null;
        }
    }


    static Integer getCellIntegerValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return null;
        switch (cell.getCellType()) {
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            case STRING:
                return Integer.parseInt(cell.getStringCellValue());
            default:
                return null;
        }
    }


    static Double getCellDoubleValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return null;
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                return Double.parseDouble(cell.getStringCellValue());
            default:
                return null;
        }
    }


    static Integer getCellFormularValue(Cell cell) {
        if(cell == null || cell.getCellType() == CellType.BLANK) return null;
        if(cell.getCellType() == CellType.FORMULA && cell.getCachedFormulaResultType() == CellType.STRING) return null;

        switch (cell.getCellType()) {
            case FORMULA, NUMERIC:
                return (int) cell.getNumericCellValue();
            default:
                return null;
        }
    }


    static LocalDateTime getCellLocalDateValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return null;
        return cell.getLocalDateTimeCellValue();
    }


    static boolean isYearBetweenXAndY(String sheetname, Integer yearX, Integer yearY){
        if(sheetname.length() != 4) return false;
        try{
            Integer.parseInt(sheetname);
            if(Integer.parseInt(sheetname) < yearX || Integer.parseInt(sheetname) > yearY) return false;
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }



}
