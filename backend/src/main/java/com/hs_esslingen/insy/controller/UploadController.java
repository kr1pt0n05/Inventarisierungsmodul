package com.hs_esslingen.insy.controller;

import com.hs_esslingen.insy.models.InventoryItem;
import com.hs_esslingen.insy.services.CSVService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/upload")
public class UploadController {

    private final CSVService csvService;

    public UploadController(CSVService csvService) {
        this.csvService = csvService;
    }

    @PostMapping("/csv")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("Upload CSV");
        List<InventoryItem> objects = csvService.readCSVFile(file);

        objects.stream().forEach(System.out::println);

        // To-Do: Validation of objects

        // To-Do: Push to database

        return ResponseEntity.ok().build();
    }
}
