package com.hs_esslingen.insy.controller;

import java.util.List;
import java.util.Set;


import com.hs_esslingen.insy.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/inventories/companies")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<String>> getAllCompanies() {
        List<String> companies = companyService.getAllCompanies();
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }
}