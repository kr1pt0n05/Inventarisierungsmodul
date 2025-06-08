package com.hs_esslingen.insy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hs_esslingen.insy.model.Extension;

public interface ExtensionRepository extends JpaRepository<Extension, Integer> {
    // Additional query methods can be defined here if needed
    // For example, to find extensions by inventory ID:
    // List<Extensions> findByInventoryId(Integer inventoryId);

}
