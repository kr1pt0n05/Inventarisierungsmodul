package com.hs_esslingen.insy.service;

import com.hs_esslingen.insy.dto.InventoryItem;
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

    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final CommentRepository commentRepository;

    public CSVService(InventoryRepository inventoriesRepository, UserRepository usersRepository, CompanyRepository companiesRepository, CommentRepository commentsRepository) {
        this.inventoryRepository = inventoriesRepository;
        this.userRepository = usersRepository;
        this.companyRepository = companiesRepository;
        this.commentRepository = commentsRepository;
    }


    @Transactional
    public void importCSVImproved(MultipartFile file) throws IOException {
        List<InventoryItem> objects = readCSVFile(file);


        // To-Do: Validation of objects


        // To-Do: Push to database
        OffsetTime now = OffsetTime.now();

        Map<String, User> usersMap = new HashMap<>();
        Map<String, Company> companiesMap = new HashMap<>();

        List<Inventory> inventoriesList = new ArrayList<>();
        List<Comment> commentsList = new ArrayList<>();

        Set<String> existingUsers = userRepository.findAll().stream().map(User::getName).collect(Collectors.toSet());
        Set<String> existingCompanies = companyRepository.findAll().stream().map(Company::getName).collect(Collectors.toSet());

        Set<String> csvObjectsUsernames = new HashSet<>();
        Set<String> csvObjectsCompanies = new HashSet<>();


        objects.forEach(obj -> {
            if(!obj.getOrderer().isEmpty() && !existingUsers.contains(obj.getOrderer())) csvObjectsUsernames.add(obj.getOrderer());
            if(!obj.getCompany().isEmpty() && !existingCompanies.contains(obj.getCompany())) csvObjectsCompanies.add(obj.getCompany());
        });

        csvObjectsUsernames.forEach(obj -> {
            usersMap.put(obj, new User(obj));
        });
        csvObjectsCompanies.forEach(obj -> {
            companiesMap.put(obj, new Company(obj));
        });


        // Create InventoryItems
        objects.forEach(obj -> {

            try {
                // 1. Create new inventory item & push to database

                User user = usersMap.get(obj.getOrderer());
                Company company = companiesMap.get(obj.getCompany());

                Inventory inventoryItem = new Inventory();
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
                    Comment comment = new Comment();
                    comment.setDescription(obj.getComment());
                    comment.setAuthor(user);
                    comment.setInventories(inventoryItem);
                    commentsList.add(comment);
                }

            }catch (Exception e){
                System.out.println(e.getMessage());
            }

        });
        userRepository.saveAll(usersMap.values());
        companyRepository.saveAll(companiesMap.values());
        inventoryRepository.saveAll(inventoriesList);
        commentRepository.saveAll(commentsList);
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
