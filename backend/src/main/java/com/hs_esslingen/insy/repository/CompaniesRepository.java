package com.hs_esslingen.insy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hs_esslingen.insy.model.Companies;

public interface CompaniesRepository extends JpaRepository<Companies, Integer> {
    // Custom query methods can be defined here if needed
    
}
