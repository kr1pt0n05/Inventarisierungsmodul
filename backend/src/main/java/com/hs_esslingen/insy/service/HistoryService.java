package com.hs_esslingen.insy.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.dto.HistoryResponseDTO;
import com.hs_esslingen.insy.model.History;
import com.hs_esslingen.insy.repository.HistoryRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class HistoryService {

    private final HistoryRepository historyRepository;

    /**
     * Retrieves the history of changes for a specific inventory item by its ID.
     * The history is sorted by creation date in ascending order.
     *
     * @param id the ID of the inventory item
     * @return a ResponseEntity containing a list of HistoryResponseDTO objects
     */
    public ResponseEntity<List<HistoryResponseDTO>> getHistory(Integer id) {
        List<History> history = historyRepository.getHistoriesByInventory_IdOrderByCreatedAtAsc(id);
        List<HistoryResponseDTO> historyDTO = history.stream().map(entry -> {
            HistoryResponseDTO historyResponseDTO = new HistoryResponseDTO();
            historyResponseDTO.setId(entry.getId());
            historyResponseDTO.setChangedBy(entry.getAuthor().getName());
            historyResponseDTO.setAttributeChanged(entry.getAttributeChanged());
            historyResponseDTO.setValueFrom(entry.getValueFrom());
            historyResponseDTO.setValueTo(entry.getValueTo());
            historyResponseDTO.setCreatedAt(entry.getCreatedAt());
            return historyResponseDTO;
        }).toList();
        return ResponseEntity.ok(historyDTO);
    }
}
