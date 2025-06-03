package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.stream.Collectors;
import com.hs_esslingen.insy.model.Company;
import com.hs_esslingen.insy.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    // Get Companies from repository
    public List<String> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(Company::getName)
                .collect(Collectors.toList());
    }
}