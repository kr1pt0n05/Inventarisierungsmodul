package com.hs_esslingen.insy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hs_esslingen.insy.model.Inventory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface InventoryRepository
        extends JpaRepository<Inventory, Integer>, JpaSpecificationExecutor<Inventory> {
     // Define custom query methods here if needed
        @Query("SELECT i.location FROM Inventory i WHERE i.location IS NOT NULL ORDER BY i.location ASC")
        Set<String> findAllLocations();

        @Query("SELECT i.serialNumber FROM Inventory i WHERE i.serialNumber IS NOT NULL ORDER BY i.serialNumber ASC")
        Set<String> findAllSerialNumbers();

        @Query("SELECT i.id FROM Inventory i WHERE i.id IN :inventoriesIds")
        Set<Integer> findInventoriesIdIn(@Param("inventoriesIds") List<Integer> inventoriesIds);

        @Query("SELECT MAX(i.price) from Inventory i")
        BigDecimal findMaxPrice();

        @Query("SELECT MIN(i.price) from Inventory i")
        Integer findMinPrice();

        @Query("SELECT MAX(i.id) from Inventory i")
        Integer findMaxId();

        @Query("SELECT MIN(i.id) from Inventory i")
        Integer findMinId();
}
