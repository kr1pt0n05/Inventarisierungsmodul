package com.hs_esslingen.insy.services;

import com.hs_esslingen.insy.DTO.InventoryItem;
import com.hs_esslingen.insy.model.*;
import com.hs_esslingen.insy.repository.*;
import com.hs_esslingen.insy.utils.StringParser;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.time.OffsetTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CSVService {
    private final Character DELIMITER = ';';

    private final InventoriesRepository inventoriesRepository;
    private final CostCentersRepository costCentersRepository;
    private final UsersRepository usersRepository;
    private final CompaniesRepository companiesRepository;
    private final CommentsRepository commentsRepository;

    public CSVService(InventoriesRepository inventoriesRepository, CostCentersRepository costCentersRepository, UsersRepository usersRepository, CompaniesRepository companiesRepository, CommentsRepository commentsRepository) {
        this.inventoriesRepository = inventoriesRepository;
        this.costCentersRepository = costCentersRepository;
        this.usersRepository = usersRepository;
        this.companiesRepository = companiesRepository;
        this.commentsRepository = commentsRepository;
    }


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

        csvObjectsUsernames.parallelStream().forEach(user -> {
            usersMap.put(user, new Users(user));
        });

        csvObjectsCompanies.parallelStream().forEach(comp -> {
            companiesMap.put(comp, new Companies(comp));
        });

/*        csvObjectsUsernames.forEach(obj -> {
            usersMap.put(obj, new Users(obj));
        });
        csvObjectsCompanies.forEach(obj -> {
            companiesMap.put(obj, new Companies(obj));
        });*/


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
                inventoryItem.setCreatedAt(now);
                inventoryItem.setDeletedAt(null);
                inventoryItem.setCompany(company);
                inventoryItem.setUser(user);

                inventoriesList.add(inventoryItem);

                // Create comments
                if(!obj.getComment().isEmpty()){
                    Comments comment = new Comments();
                    comment.setDescription(obj.getComment());
                    comment.setAuthor(user);
                    comment.setCreatedAt(now);
                    comment.setInventories(inventoryItem);
                    commentsList.add(comment);
                }

            }catch (ParseException e){
                System.out.println(e.getMessage());

            }catch (Exception e){
                System.out.println(e.getMessage());
            }

        });
        usersRepository.saveAll(usersMap.values());
        companiesRepository.saveAll(companiesMap.values());
        inventoriesRepository.saveAll(inventoriesList);
        commentsRepository.saveAll(commentsList);
    }


    public void importCSV(MultipartFile file) throws IOException {
        List<InventoryItem> objects = readCSVFile(file);

        objects.stream().forEach(System.out::println);
        System.out.println("\n--------------\nAnzahl Objekte: " + objects.size());

        // To-Do: Validation of objects


        // To-Do: Push to database


        // Uncomment since CostCenters can not be parsed yet: Fix!
/*        // Create costCenters that do not already exist
        objects.stream().forEach(obj -> {
            CostCenters costCenter = new CostCenters();

            boolean exists = costCentersRepository.existsById(obj.getCostCenter());
            if (!exists) {
                System.out.println(obj.getCostCenter());
                costCenter.setId(obj.getCostCenter());
            }
        });*/

        // Create users if not already exist
        objects.stream().forEach(obj -> {
            Users user = new Users();
            String username = obj.getOrderer();

            // Avoid creating users from empty strings
            if (!username.isEmpty()){
                user.setName(obj.getOrderer());
                boolean exists = usersRepository.exists(Example.of(user));
                if(!exists) usersRepository.save(user);
            }
        });


        // Create companies if not already exist
        objects.forEach(obj -> {
            Companies company = new Companies();
            String name = obj.getCompany();

            // Avoid creating companies from empty strings
            if (!name.isEmpty()){
                company.setName(obj.getCompany());
                boolean exists = companiesRepository.exists(Example.of(company));
                if(!exists) companiesRepository.save(company);
            }
        });

        // Create InventoryItems
        objects.forEach(obj -> {

            try {
                // 1. Create new inventory item & push to database

                Users user = usersRepository.getUsersByName(obj.getOrderer());
                Companies company = companiesRepository.getCompaniesByName(obj.getCompany());

                Inventories inventoryItem = new Inventories();
                inventoryItem.setId(Integer.parseInt(obj.getInventoryNumber()));
                inventoryItem.setDescription(obj.getDescription());
                inventoryItem.setSerialNumber(obj.getSerialNumber());
                inventoryItem.setIsDeinventoried(false); // This is basically impossible to check through a .csv file
                inventoryItem.setPrice(StringParser.parseString(obj.getPrice()));
                inventoryItem.setLocation(obj.getLocation());
                inventoryItem.setCreatedAt(OffsetTime.now());
                inventoryItem.setDeletedAt(null);
                inventoryItem.setCompany(company);
                inventoryItem.setUser(user);


                inventoriesRepository.save(inventoryItem);

                // Create comments
                if(!obj.getComment().isEmpty()){
                    Comments comment = new Comments();
                    comment.setDescription(obj.getComment());
                    comment.setAuthor(user);
                    comment.setCreatedAt(OffsetTime.now());
                    comment.setInventories(inventoryItem);
                    commentsRepository.save(comment);
                }

            }catch (ParseException e){
                System.out.println(e.getMessage());

            }catch (Exception e){
                System.out.println(e.getMessage());
            }

        });
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
