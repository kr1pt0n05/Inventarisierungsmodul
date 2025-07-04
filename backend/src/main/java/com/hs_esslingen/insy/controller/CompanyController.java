package com.hs_esslingen.insy.controller;


import com.hs_esslingen.insy.dto.CompanyDTO;
import com.hs_esslingen.insy.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<CompanyDTO> getAllCompanies() {
        CompanyDTO companies = companyService.getAllCompanies();
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }
}