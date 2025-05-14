package com.hs_esslingen.insy.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hs_esslingen.insy.model.Inventories;


public interface InventoriesRepository extends JpaRepository<Inventories, Integer> {

    List<Inventories> getAllInventories();
    
}
