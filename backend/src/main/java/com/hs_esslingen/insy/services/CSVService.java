package com.hs_esslingen.insy.services;

import com.hs_esslingen.insy.DTO.InventoryItem;
import com.hs_esslingen.insy.model.*;
import com.hs_esslingen.insy.repository.*;
import com.hs_esslingen.insy.utils.StringParser;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
