package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.hs_esslingen.insy.model.Companies;
import com.hs_esslingen.insy.repository.CompaniesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompaniesRepository companiesRepository;

    // Get Companies from repository
    public List<String> getAllCompanies() {
        return companiesRepository.findAll().stream()
                .map(Companies::getName)
                .collect(Collectors.toList());
    }
}