package com.hs_esslingen.insy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hs_esslingen.insy.model.Companies;

@Repository
public interface CompaniesRepository extends JpaRepository<Companies, Integer> {
    // Custom query methods can be defined here if needed
    // For example, to find a company by name:
    @Query("SELECT c FROM Companies c WHERE c.name = ?1")
    Optional<Companies> findByName(String name);

    Companies getCompaniesByName(String name);
}
