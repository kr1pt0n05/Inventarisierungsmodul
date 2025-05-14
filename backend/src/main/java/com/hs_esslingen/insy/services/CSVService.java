package com.hs_esslingen.insy.services;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class CSVService {

    private final String DELIMITER = ";";

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
    public List<Object> readCSVFile(MultipartFile file) throws IOException {
        InputStream stream = file.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        List<Object> csv = new CsvToBeanBuilder(reader)
                .withIgnoreEmptyLine(true)
                .withIgnoreLeadingWhiteSpace(true)
                .withType(Object.class)
                .build()
                .parse();

        return csv;
    }

}
