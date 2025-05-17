package com.hs_esslingen.insy.controller;

import com.hs_esslingen.insy.DTO.InventoryItem;
import com.hs_esslingen.insy.model.Inventories;
import com.hs_esslingen.insy.services.CSVService;
import com.hs_esslingen.insy.utils.StringParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.OffsetTime;
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
        csvService.importCSV(file);

        return ResponseEntity.ok().build();
    }
}
