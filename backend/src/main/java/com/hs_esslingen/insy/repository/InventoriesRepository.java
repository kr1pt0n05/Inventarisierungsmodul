package com.hs_esslingen.insy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hs_esslingen.insy.model.Inventories;

import java.util.List;
import java.util.Set;

@Repository
public interface InventoriesRepository
        extends JpaRepository<Inventories, Integer>, JpaSpecificationExecutor<Inventories> {
    // Define custom query methods here if needed

    @Query("SELECT i.id FROM Inventories i WHERE i.id IN :inventoriesIds")
    Set<Integer> findInventoriesIdIn(@Param("inventoriesIds") List<Integer> inventoriesIds);

}
