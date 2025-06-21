package com.hs_esslingen.insy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hs_esslingen.insy.dto.HistoryResponseDTO;
import com.hs_esslingen.insy.service.HistoryService;

import lombok.AllArgsConstructor;

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
