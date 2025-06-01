package com.hs_esslingen.insy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hs_esslingen.insy.model.Inventories;

import java.util.List;
import java.util.Set;

@Repository
public interface InventoriesRepository
        extends JpaRepository<Inventories, Integer>, JpaSpecificationExecutor<Inventories> {
     // Define custom query methods here if needed
        @Query("SELECT i.location FROM Inventories i")
        Set<String> findAllLocations();

        @Query("SELECT i.serialNumber FROM Inventories i")
        Set<String> findAllSerialNumbers();
}
