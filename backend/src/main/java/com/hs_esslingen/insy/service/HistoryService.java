package com.hs_esslingen.insy.service;

import java.util.List;

import com.hs_esslingen.insy.dto.HistoryResponseDTO;
import com.hs_esslingen.insy.exception.NotFoundException;
import com.hs_esslingen.insy.model.History;
import com.hs_esslingen.insy.repository.HistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class HistoryService {

    private final HistoryRepository historyRepository;

    // TODO: Use Mapstruct and fetch all authors at once
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
