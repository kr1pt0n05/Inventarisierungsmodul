package com.hs_esslingen.insy.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.hs_esslingen.insy.dto.InventoriesResponseDTO;
import com.hs_esslingen.insy.dto.InventoryCreateRequestDTO;
import com.hs_esslingen.insy.service.InventoryService;


@RestController
@RequestMapping("/inventories")
public class InventoryController {

    private final InventoryService inventoriesService;

    InventoryController(InventoryService inventoriesService) {
        this.inventoriesService = inventoriesService;
    }

    // Get all elements from the inventory list
    @GetMapping
    public Page<InventoriesResponseDTO> getAllInventories(
            @RequestParam(name = "tags", required = false) List<Integer> tags,
            @RequestParam(name = "minId", required = false) Integer minId,
            @RequestParam(name = "maxId", required = false) Integer maxId,
            @RequestParam(name = "minPrice", required = false) Integer minPrice,
            @RequestParam(name = "maxPrice", required = false) Integer maxPrice,
            @RequestParam(name = "isDeinventoried", required = false) Boolean isDeinventoried,
            @RequestParam(name = "orderer", required = false) List<String> orderers,
            @RequestParam(name = "company", required = false) List<String> companies,
            @RequestParam(name = "location", required = false) List<String> locations,
            @RequestParam(name = "costCenter", required = false) List<String> costCenters,
            @RequestParam(name = "serialNumber", required = false) List<String> serialNumbers,
            @RequestParam(name = "createdAfter", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAfter,
            @RequestParam(name = "createdBefore", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdBefore,
            @RequestParam(name = "orderBy", required = false, defaultValue = "id") String orderBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "searchText", required = false) String searchText,
            @PageableDefault(size = 50) Pageable pageable) {

        return inventoriesService.getAllInventories(tags, minId, maxId, minPrice, maxPrice,
                isDeinventoried, orderers, companies, locations, costCenters, serialNumbers, createdAfter,
                createdBefore, orderBy, direction, searchText, pageable);
    }

    // Get one element from the inventory list
    @GetMapping("/{id}")
    public ResponseEntity<InventoriesResponseDTO> getInventoryById(@PathVariable("id") Integer id) {

        return inventoriesService.getInventoryById(id);
    }

    // Add one element to the inventory list
    @PostMapping
    public ResponseEntity<InventoriesResponseDTO> addInventory(@RequestBody InventoryCreateRequestDTO requestDTO) {
        InventoriesResponseDTO responseDTO = inventoriesService.addInventory(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // Delete one element from the inventory list
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Integer id) {
        return inventoriesService.deleteInventory(id);
    }

    // Update one element in the inventory list
    @PatchMapping("/{id}")
    public ResponseEntity<InventoriesResponseDTO> updateInventory(@PathVariable Integer id,
            @RequestBody Map<String, Object> patchData) {

        return inventoriesService.updateInventory(id, patchData);
    }
}
