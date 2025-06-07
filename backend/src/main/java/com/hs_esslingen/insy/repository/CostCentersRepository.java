package com.hs_esslingen.insy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hs_esslingen.insy.model.CostCenters;

import java.util.List;

@Repository
public interface CostCentersRepository extends JpaRepository<CostCenters, Integer> {
    // Custom query methods can be defined here if needed
    @Query("SELECT c FROM CostCenters c WHERE c.description = ?1")
    CostCenters findByName(String name);

    @Query("SELECT c.description FROM CostCenters c")
    List<String> findAllCostCenterDescriptions();

}
