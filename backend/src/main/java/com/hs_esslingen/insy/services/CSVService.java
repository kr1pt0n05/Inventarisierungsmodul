package com.hs_esslingen.insy.services;

import com.hs_esslingen.insy.DTO.InventoryItem;
import com.hs_esslingen.insy.model.Inventories;
import com.hs_esslingen.insy.repository.InventoriesRepository;
import com.hs_esslingen.insy.utils.StringParser;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.time.OffsetTime;
import java.util.List;

@Service
public class CSVService {
    private final Character DELIMITER = ';';

    private final InventoriesRepository inventoriesRepository;

    public CSVService(InventoriesRepository inventoriesRepository) {
        this.inventoriesRepository = inventoriesRepository;
    }


    public void importCSV(MultipartFile file) throws IOException {
        List<InventoryItem> objects = readCSVFile(file);

        objects.stream().forEach(System.out::println);
        System.out.println("\n--------------\nAnzahl Objekte: " + objects.size());

        // To-Do: Validation of objects


        // To-Do: Push to database
        // 1. Create new inventory item & push to database

        objects.stream().forEach(obj -> {

            try {
                Inventories inventoryItem = new Inventories();
                inventoryItem.setId(Integer.parseInt(obj.getInventoryNumber()));
                inventoryItem.setDescription(obj.getDescription());
                inventoryItem.setSerialNumber(obj.getSerialNumber());
                inventoryItem.setIsDeinventoried(false); // This is basically impossible to check through a .csv file
                inventoryItem.setPrice(StringParser.parseString(obj.getPrice()));
                inventoryItem.setLocation(obj.getLocation());
                inventoryItem.setCreatedAt(OffsetTime.now());
                inventoryItem.setDeletedAt(null);

                inventoriesRepository.save(inventoryItem);


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
