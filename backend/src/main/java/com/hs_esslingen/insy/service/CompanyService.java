package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.exception.BadRequest;
import com.hs_esslingen.insy.model.Company;
import com.hs_esslingen.insy.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;

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

    public Optional<Company> findCompanyByName(String company) {
        return companyRepository.findByName(company);
    }

    public Optional<Company> findCompanyById(Integer id) {
        return companyRepository.findById(id);
    }

    public Company resolveCompany(Object company) {
        if (company instanceof Integer companyId) {
            return companyRepository.findById(companyId)
                    .orElseThrow(() -> new BadRequest("Couldn't find company with id: " + companyId));
        } else if (company instanceof String companyName) {
            return companyRepository.findByName(companyName)
                    .orElseGet(() -> companyRepository.save(new Company(companyName)));
        }
        throw new BadRequest("Company must be of type Integer or String");
    }

    public Company createCompanyByName(String name) {
        Company company = new Company(name);
        return companyRepository.save(company);
    }
}