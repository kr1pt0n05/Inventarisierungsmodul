package com.hs_esslingen.insy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hs_esslingen.insy.service.CSVService;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class UploadController {

    private final CSVService csvService;

    public UploadController(CSVService csvService) {
        this.csvService = csvService;
    }

    @PostMapping("/excel")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) throws IOException {
        csvService.importExcel(file);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/csv")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) throws IOException {
        csvService.importCSVImproved(file);

        return ResponseEntity.ok().build();
    }
}
