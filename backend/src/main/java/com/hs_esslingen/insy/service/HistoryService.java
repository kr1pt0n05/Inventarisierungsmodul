package com.hs_esslingen.insy.service;

import java.util.List;

import com.hs_esslingen.insy.model.History;
import com.hs_esslingen.insy.repository.HistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class HistoryService {

    private final HistoryRepository historyRepository;

    public ResponseEntity<List<History>> getHistory(Integer id) {
        List<History> history = historyRepository.getHistoriesByInventory_IdOrderByCreatedAtAsc(id);
        return ResponseEntity.ok(history);

    }
}
