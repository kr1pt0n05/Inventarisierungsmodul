package com.hs_esslingen.insy.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hs_esslingen.insy.model.Inventories;

@Repository
public interface InventoriesRepository extends JpaRepository<Inventories, Integer> {

    // Define custom query methods here if needed
    
}
