package com.hs_esslingen.insy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hs_esslingen.insy.model.InventoryTagRelations;
import com.hs_esslingen.insy.configuration.InventoryTagKey;
import com.hs_esslingen.insy.model.Inventories;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface InventoryTagRelationsRepository extends JpaRepository<InventoryTagRelations, InventoryTagKey> {

    @Query("SELECT r.inventory FROM InventoryTagRelations r WHERE r.tag.id = :tagId")
    List<Inventories> findInventoriesByTagId(@Param("tagId") Integer tagId);
}