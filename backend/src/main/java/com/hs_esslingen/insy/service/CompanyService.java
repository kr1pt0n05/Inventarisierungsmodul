package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.hs_esslingen.insy.exception.NotFoundException;
import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.dto.CompanyDTO;
import com.hs_esslingen.insy.exception.BadRequestException;
import com.hs_esslingen.insy.model.Company;
import com.hs_esslingen.insy.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;

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

    public Optional<Company> findCompanyByName(String company) {
        return companyRepository.findByName(company);
    }

    public Optional<Company> findCompanyById(Integer id) {
        return companyRepository.findById(id);
    }

    public Company resolveCompany(Object company) {
        if (company instanceof Integer companyId) {
            return companyRepository.findById(companyId)
                    .orElseThrow(() -> new NotFoundException("Company with id: " + companyId + " not found"));
        } else if (company instanceof String companyName) {
            return companyRepository.findByName(companyName)
                    .orElseGet(() -> companyRepository.save(new Company(companyName)));
        }
        throw new BadRequestException("Company must be of type Integer or String");
    }

    public Company createCompanyByName(String name) {
        Company company = new Company(name);
        return companyRepository.save(company);
    }
}