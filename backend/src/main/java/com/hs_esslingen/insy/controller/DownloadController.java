package com.hs_esslingen.insy.controller;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hs_esslingen.insy.service.ExcelService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/download")
public class DownloadController {

    private final ExcelService excelService;

    @GetMapping("/xlsx")
    public ResponseEntity<Resource> downloadXlsx() throws IOException {
        return excelService.exportExcel();
    }
}
