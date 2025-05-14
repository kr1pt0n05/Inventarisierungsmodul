package com.hs_esslingen.insy.services;

import com.hs_esslingen.insy.models.InventoryItem;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class CSVService {

    private final Character DELIMITER = ';';

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
