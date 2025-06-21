package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.dto.CompanyDTO;
import com.hs_esslingen.insy.exception.BadRequestException;
import com.hs_esslingen.insy.exception.NotFoundException;
import com.hs_esslingen.insy.model.Company;
import com.hs_esslingen.insy.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    /**
     * Retrieves all companies from the repository and returns them as a CompanyDTO.
     * The companies are sorted alphabetically by their names.
     *
     * @return a CompanyDTO containing a list of all companies
     */
    public CompanyDTO getAllCompanies() {
        List<String> allCompanies = companyRepository.findAll().stream()
                .map(Company::getName)
                .sorted()
                .collect(Collectors.toList());

        return CompanyDTO.builder()
                .companies(allCompanies)
                .build();
    }

    /**
     * Finds a company by its name.
     *
     * @param company the name of the company to be found
     * @return an Optional containing the Company if found, or empty if not found
     */
    public Optional<Company> findCompanyByName(String company) {
        return companyRepository.findByName(company);
    }

    /**
     * Finds a company by its ID.
     *
     * @param id the ID of the company to be found
     * @return an Optional containing the Company if found, or empty if not found
     */
    public Optional<Company> findCompanyById(Integer id) {
        return companyRepository.findById(id);
    }

    /**
     * Resolves a company based on the provided identifier.
     * The company can be either an Integer (company ID) or a String (company name).
     *
     * @param company the identifier of the company
     * @return the resolved Company object
     * @throws NotFoundException   if the company is not found
     * @throws BadRequestException if the company is neither an Integer nor a String
     */
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

    /**
     * Creates a new company with the given name and saves it to the repository.
     *
     * @param name the name of the company to be created
     * @return the created Company object
     */
    public Company createCompanyByName(String name) {
        Company company = new Company(name);
        return companyRepository.save(company);
    }
}