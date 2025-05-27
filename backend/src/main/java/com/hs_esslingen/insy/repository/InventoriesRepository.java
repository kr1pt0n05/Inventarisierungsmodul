package com.hs_esslingen.insy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.hs_esslingen.insy.model.Inventories;

@Repository
public interface InventoriesRepository
        extends JpaRepository<Inventories, Integer>, JpaSpecificationExecutor<Inventories> {
    // Define custom query methods here if needed

}
