package com.hs_esslingen.insy.repository;

import com.hs_esslingen.insy.model.History;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {

    List<History> getHistoriesByInventory_Id(Integer inventoryId);

    List<History> getHistoriesByInventory_IdOrderByCreatedAtAsc(Integer inventoryId);
}
