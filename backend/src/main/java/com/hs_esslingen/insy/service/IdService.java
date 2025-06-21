package com.hs_esslingen.insy.service;

import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.dto.IdDTO;
import com.hs_esslingen.insy.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdService {

    private final InventoryRepository inventoryRepository;

    /**
     * Retrieves the maximum and minimum IDs from the inventory repository.
     *
     * @return an IdDTO containing the maximum and minimum IDs
     */
    public IdDTO getMaxAndMinId() {
        Integer maxId = inventoryRepository.findMaxId();
        Integer minId = inventoryRepository.findMinId();

        return IdDTO.builder()
                .maxId(maxId)
                .minId(minId)
                .build();
    }

}
