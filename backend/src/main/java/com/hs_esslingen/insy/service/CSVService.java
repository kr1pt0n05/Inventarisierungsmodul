package com.hs_esslingen.insy.service;

import com.hs_esslingen.insy.dto.InventoryExcel;
import com.hs_esslingen.insy.dto.InventoryItem;
import com.hs_esslingen.insy.model.*;
import com.hs_esslingen.insy.repository.*;
import com.hs_esslingen.insy.utils.StringParser;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetTime;
import java.util.*;
import java.util.stream.Collectors;

@Service @AllArgsConstructor
public class CSVService {
    private final Character DELIMITER = ';';

    private final InventoriesRepository inventoriesRepository;
    private final UsersRepository usersRepository;
    private final CompaniesRepository companiesRepository;
    private final CommentsRepository commentsRepository;
    private final CostCentersRepository costCentersRepository;


    public void importExcel(MultipartFile file) throws IOException {
        // Check file is not empty
        if(file.isEmpty()) throw new IllegalArgumentException("Datei darf nicht leer sein!");

        // Check file extension. Must be .xls or .xlsx
        String fileName = file.getOriginalFilename();
        if(fileName == null || !(fileName.endsWith(".xls") || fileName.endsWith(".xlsx"))
        ) throw new IllegalArgumentException("Datei muss mit .xls oder .xlsx enden!");

        // Creates appropriate Workbook (HSSFWorkbook for .xls and XSSFWorkbook for .xls)
        Workbook wb = WorkbookFactory.create(file.getInputStream());
        List<InventoryExcel> excelObjects = new ArrayList<>();


        // Parse Inventory Items of every sheet's row
        int numberOfSheets = wb.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = wb.getSheetAt(i);

            // Validate sheet name
            if(CSVService.isYearBetween2000And2100(sheet.getSheetName())){
                for (Row row : sheet) {

                    // Skip first row, since it contains headings
                    if (row.getCell(0) == null || row.getRowNum() == 0 || row.getRowNum() == 1) continue;

                    // Parse all values of excel files into one object
                    InventoryExcel inv = new InventoryExcel();
                    CellStyle style = row.getCell(1).getCellStyle();
                    Font font = wb.getFontAt(style.getFontIndex());

                    // Parse each row into InventoryExcel object
                    inv.setCostCenter(CSVService.getCellStringValue(row.getCell(0)));
                    inv.setInventoryNumber(CSVService.getCellFormularValue(row.getCell(1)));
                    inv.setAmount(CSVService.getCellIntegerValue(row.getCell(2)));
                    inv.setDescription(CSVService.getCellStringValue(row.getCell(3)));
                    inv.setCompany(CSVService.getCellStringValue(row.getCell(4)) );
                    inv.setPrice(CSVService.getCellDoubleValue(row.getCell(5)));
                    inv.setCreatedAt(CSVService.getCellLocalDateValue(row.getCell(6)));
                    inv.setSerialNumber(CSVService.getCellStringValue(row.getCell(7)) );
                    inv.setLocation(CSVService.getCellStringValue(row.getCell(8)));
                    inv.setOrderer(CSVService.getCellStringValue(row.getCell(9)));

                    inv.addComment(CSVService.getCellStringValue(row.getCell(10)));
                    inv.addComment(CSVService.getCellStringValue(row.getCell(11)));
                    inv.addComment(CSVService.getCellStringValue(row.getCell(12)));
                    inv.addComment(CSVService.getCellStringValue(row.getCell(13)));
                    inv.addComment(CSVService.getCellStringValue(row.getCell(14)));
                    inv.addComment(CSVService.getCellStringValue(row.getCell(15)));
                    inv.addComment(CSVService.getCellStringValue(row.getCell(16)));


                    inv.setDeinventoried(font.getStrikeout());
                    excelObjects.add(inv);
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
        Set<Integer> existingInventoryNumbers = inventoriesRepository.findInventoriesIdIn(excelObjects.stream().map(InventoryExcel::getInventoryNumber).toList());
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
        List<CostCenters> existingCostCenters = costCentersRepository.findByDescriptionIn(excelCostCenters);
        // Remove the already existing costCenters from the excel's costCenters
        excelCostCenters.removeAll(existingCostCenters.stream().map(CostCenters::getDescription).toList());
        // Persist the now remaining excel sheets costCenters (all costCenters that are new and do not already exist in the DB)
        // to the database;
         List<CostCenters> costCenters = costCentersRepository.saveAll(excelCostCenters.stream().map(CostCenters::new).toList());
        // finally merge the excel sheet costCenters (that were just persisted) and the database costCenters (that already existed)
        // and put them in a map for fast & easy access
        Map<String, CostCenters> costCentersMap = new HashMap<>();
        existingCostCenters.forEach(cc -> costCentersMap.put(cc.getDescription(), cc));
        costCenters.forEach(cc -> costCentersMap.put(cc.getDescription(), cc));


        List<Users> existingUsers = usersRepository.findByNameIn(excelUsers);
        excelUsers.removeAll(existingUsers.stream().map(Users::getName).toList());
        List<Users> users = usersRepository.saveAll(excelUsers.stream().map(Users::new).toList());
        Map<String, Users> usersMap = new HashMap<>();
        existingUsers.forEach(u -> usersMap.put(u.getName(), u));
        users.forEach(u -> usersMap.put(u.getName(), u));


        List<Companies> existingCompanies = companiesRepository.findByNameIn(excelCompanies);
        excelCompanies.removeAll(existingCompanies.stream().map(Companies::getName).toList());
        List<Companies> companies = companiesRepository.saveAll(excelCompanies.stream().map(Companies::new).toList());
        Map<String, Companies> companiesMap = new HashMap<>();
        existingCompanies.forEach(c -> companiesMap.put(c.getName(), c));
        companies.forEach(c -> companiesMap.put(c.getName(), c));


        // Put Inventory Items to a map, for easier access when creating comments
        Map<Integer, Inventories> inventory = new HashMap<>();
        excelObjects.forEach(obj -> {
            Inventories inv = new Inventories();
            inv.setId(obj.getInventoryNumber());
            inv.setCostCenter(costCentersMap.getOrDefault(obj.getCostCenter(), null));
            inv.setUser(usersMap.getOrDefault(obj.getOrderer(), null));
            inv.setCompany(companiesMap.getOrDefault(obj.getCompany(), null));
            inv.setDescription(obj.getDescription());
            inv.setSerialNumber(obj.getSerialNumber());
            inv.setIsDeinventoried(obj.isDeinventoried());
            inv.setPrice(BigDecimal.valueOf(obj.getPrice()));
            inv.setLocation(obj.getLocation());
            inventory.put(obj.getInventoryNumber(), inv);
        });
        inventoriesRepository.saveAll(inventory.values());


        List<Comments> comments = new ArrayList<>();
        excelComments.forEach((invNumber, commentsList) -> {
            Inventories inv = inventory.get(invNumber);
            commentsList.forEach(comment -> {
                Comments c = new Comments();
                c.setInventories(inv);
                c.setDescription(comment);
                comments.add(c);

            });
        });
        commentsRepository.saveAll(comments);

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
                System.out.println("Parsing Int from StringCellvalue");
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
            case FORMULA:
                return (int) cell.getNumericCellValue();
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            default:
                return null;
        }
    }


    static LocalDateTime getCellLocalDateValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return null;
        return cell.getLocalDateTimeCellValue();
    }


    static boolean isYearBetween2000And2100(String sheetname){
        if(sheetname.length() != 4) return false;
        try{
            Integer.parseInt(sheetname);
            if(Integer.parseInt(sheetname) < 2000 || Integer.parseInt(sheetname) > 2100) return false;
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }



    @Transactional
    public void importCSVImproved(MultipartFile file) throws IOException {
        List<InventoryItem> objects = readCSVFile(file);


        // To-Do: Validation of objects


        // To-Do: Push to database
        OffsetTime now = OffsetTime.now();

        Map<String, Users> usersMap = new HashMap<>();
        Map<String, Companies> companiesMap = new HashMap<>();

        List<Inventories> inventoriesList = new ArrayList<>();
        List<Comments> commentsList = new ArrayList<>();

        Set<String> existingUsers = usersRepository.findAll().stream().map(Users::getName).collect(Collectors.toSet());
        Set<String> existingCompanies = companiesRepository.findAll().stream().map(Companies::getName).collect(Collectors.toSet());

        Set<String> csvObjectsUsernames = new HashSet<>();
        Set<String> csvObjectsCompanies = new HashSet<>();


        objects.forEach(obj -> {
            if(!obj.getOrderer().isEmpty() && !existingUsers.contains(obj.getOrderer())) csvObjectsUsernames.add(obj.getOrderer());
            if(!obj.getCompany().isEmpty() && !existingCompanies.contains(obj.getCompany())) csvObjectsCompanies.add(obj.getCompany());
        });

        csvObjectsUsernames.forEach(obj -> {
            usersMap.put(obj, new Users(obj));
        });
        csvObjectsCompanies.forEach(obj -> {
            companiesMap.put(obj, new Companies(obj));
        });


        // Create InventoryItems
        objects.forEach(obj -> {

            try {
                // 1. Create new inventory item & push to database

                Users user = usersMap.get(obj.getOrderer());
                Companies company = companiesMap.get(obj.getCompany());

                Inventories inventoryItem = new Inventories();
                inventoryItem.setId(Integer.parseInt(obj.getInventoryNumber()));
                inventoryItem.setDescription(obj.getDescription());
                inventoryItem.setSerialNumber(obj.getSerialNumber());
                inventoryItem.setIsDeinventoried(false); // This is basically impossible to check through a .csv file
                inventoryItem.setPrice(StringParser.parseString(obj.getPrice()));
                inventoryItem.setLocation(obj.getLocation());
                inventoryItem.setCompany(company);
                inventoryItem.setUser(user);

                inventoriesList.add(inventoryItem);

                // Create comments
                if(!obj.getComment().isEmpty()){
                    Comments comment = new Comments();
                    comment.setDescription(obj.getComment());
                    comment.setAuthor(user);
                    comment.setInventories(inventoryItem);
                    commentsList.add(comment);
                }

            }catch (Exception e){
                System.out.println(e.getMessage());
            }

        });
        usersRepository.saveAll(usersMap.values());
        companiesRepository.saveAll(companiesMap.values());
        inventoriesRepository.saveAll(inventoriesList);
        commentsRepository.saveAll(commentsList);
    }


    /*
     * BufferReader class is the preferred way for reading files.
     * It will simply put, buffer the contents of a Reader and speed up I/O operations.
     *
     * Therefore, BufferedReader needs a Reader as parameter.
     * We will use InputStreamReader, since we are reading from a non-persistent file in RAM.
     * For InputStreamReader to read our file, it needs an InputStream.
     * So we need to convert our MultipartFile into an InputStream.
     *
     * */
    public List<InventoryItem> readCSVFile(@RequestParam("file") MultipartFile file) throws IOException {
        InputStream stream = file.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));

        List<InventoryItem> csv = new CsvToBeanBuilder(br)
                .withType(InventoryItem.class)
                .withSeparator(DELIMITER)
                .build()
                .parse();

        return csv;
    }
}
