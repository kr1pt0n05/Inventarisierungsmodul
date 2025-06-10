package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.stream.Collectors;

import com.hs_esslingen.insy.dto.CompanyDTO;
import com.hs_esslingen.insy.dto.CostCenterDTO;
import com.hs_esslingen.insy.model.Company;
import com.hs_esslingen.insy.model.CostCenter;
import com.hs_esslingen.insy.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    // Change return type from List<CompanyDTO> to CompanyDTO
    public CompanyDTO getAllCompanies() {
        List<String> allCompanies = companyRepository.findAll().stream()
                .map(Company::getName)
                .sorted()
                .collect(Collectors.toList());

        return CompanyDTO.builder()
                .companies(allCompanies)
                .build();
    }
}