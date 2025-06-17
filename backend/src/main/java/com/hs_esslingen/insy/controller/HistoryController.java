package com.hs_esslingen.insy.controller;

import com.hs_esslingen.insy.dto.HistoryResponseDTO;
import com.hs_esslingen.insy.model.History;
import com.hs_esslingen.insy.service.HistoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/inventories/{id}/history")
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping
    public ResponseEntity<List<HistoryResponseDTO>> getHistory(@PathVariable("id") Integer id) {
        return historyService.getHistory(id);
    }
}
