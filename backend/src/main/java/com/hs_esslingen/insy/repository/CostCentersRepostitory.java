package com.hs_esslingen.insy.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.hs_esslingen.insy.model.CostCenters;

public interface CostCentersRepostitory extends JpaRepository<CostCenters, Integer> {
    // Custom query methods can be defined here if needed
    CostCenters findByDescriptionContainingCostCenters(String name);

}
