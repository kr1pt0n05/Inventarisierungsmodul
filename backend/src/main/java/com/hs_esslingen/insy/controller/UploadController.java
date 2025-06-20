package com.hs_esslingen.insy.controller;

import com.hs_esslingen.insy.service.ExcelService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@AllArgsConstructor
@RestController
@RequestMapping("/upload")
public class UploadController {
    
    private final ExcelService excelService;


    @PostMapping("/excel")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) throws IOException {
        excelService.importExcel(file);
        return ResponseEntity.ok().build();
    }

}
