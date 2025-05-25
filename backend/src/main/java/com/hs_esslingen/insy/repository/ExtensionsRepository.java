package com.hs_esslingen.insy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hs_esslingen.insy.model.Extensions;

public interface ExtensionsRepository extends JpaRepository<Extensions, Integer> {
    // Additional query methods can be defined here if needed
    // For example, to find extensions by inventory ID:
    // List<Extensions> findByInventoryId(Integer inventoryId);

}
