package com.hs_esslingen.insy.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hs_esslingen.insy.model.CostCenters;

@Repository
public interface CostCentersRepository extends JpaRepository<CostCenters, String> {
    // Custom query methods can be defined here if needed
    @Query("SELECT c FROM CostCenters c WHERE c.description = ?1")
    CostCenters findByDescriptionContainingCostCenters(String name);

}
