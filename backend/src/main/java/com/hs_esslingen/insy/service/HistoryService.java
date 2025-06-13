package com.hs_esslingen.insy.service;

<<<<<<< backend-orders
import java.util.List;

=======
import com.hs_esslingen.insy.model.History;
import com.hs_esslingen.insy.repository.HistoryRepository;
import lombok.AllArgsConstructor;
>>>>>>> main
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.model.History;
import com.hs_esslingen.insy.repository.HistoryRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class HistoryService {

    private final HistoryRepository historyRepository;

    public ResponseEntity<List<History>> getHistory(Integer id) {
        List<History> history = historyRepository.getHistoriesByInventory_IdOrderByCreatedAtAsc(id);
        return ResponseEntity.ok(history);

    }
}
