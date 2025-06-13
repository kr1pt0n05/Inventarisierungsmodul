package com.hs_esslingen.insy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hs_esslingen.insy.model.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {

    List<History> getHistoriesByInventory_Id(Integer inventoryId);

    List<History> getHistoriesByInventory_IdOrderByCreatedAtAsc(Integer inventoryId);
}
