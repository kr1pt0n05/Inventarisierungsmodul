package com.hs_esslingen.insy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hs_esslingen.insy.model.CostCenter;

import java.util.Collection;
import java.util.List;

@Repository
public interface CostCenterRepository extends JpaRepository<CostCenter, Integer> {
    // Custom query methods can be defined here if needed
    @Query("SELECT c FROM CostCenter c WHERE c.description = ?1")
    CostCenter findByName(String name);

    List<CostCenter> findByDescriptionIn(Collection<String> descriptions);

}
