package com.hs_esslingen.insy.controller;

import java.util.List;
import java.util.Map;

import com.hs_esslingen.insy.dto.InventoriesCreateRequestDTO;
import com.hs_esslingen.insy.dto.InventoriesResponseDTO;
import com.hs_esslingen.insy.service.InventoriesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventories")
public class InventoriesController {

    @Autowired
    private InventoriesService inventoriesService;

    // Alle Elemente der Inventarisierungsliste abrufen
    @GetMapping
    public Page<InventoriesResponseDTO> getAllInventories(
            @RequestParam(required = false) List<Integer> tags,
            @RequestParam(required = false) Integer minId,
            @RequestParam(required = false) Integer maxId,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Boolean isDeinventoried,
            @PageableDefault(size = 50) Pageable pageable) {

        return inventoriesService.getAllInventories(tags, minId, maxId, minPrice, maxPrice, isDeinventoried, pageable);
    }

    // Ein Element der Inventarisierungsliste abrufen
    @GetMapping("/{id}")
    public ResponseEntity<InventoriesResponseDTO> getInventoryById(@PathVariable("id") Integer id) {

        return inventoriesService.getInventoryById(id);
    }

    // Ein Element der Inventarisierungsliste hinzufügen
    @PostMapping
    public ResponseEntity<InventoriesResponseDTO> addInventory(@RequestBody InventoriesCreateRequestDTO requestDTO) {
        InventoriesResponseDTO responseDTO = inventoriesService.addInventory(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // Ein Element der Inventarisierungsliste löschen
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Integer id) {
        return inventoriesService.deleteInventory(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<InventoriesResponseDTO> updateInventory(@PathVariable Integer id,
            @RequestBody Map<String, Object> patchData) {

        return inventoriesService.updateInventory(id, patchData);
    }
}
