package com.hs_esslingen.insy.service;

import com.hs_esslingen.insy.dto.IdDTO;
import com.hs_esslingen.insy.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class IdService {

    private final InventoryRepository inventoryRepository;

    public IdDTO getMaxAndMinId() {
        Integer maxId = inventoryRepository.findMaxId();
        Integer minId = inventoryRepository.findMinId();

        return IdDTO.builder()
                .maxId(maxId)
                .minId(minId)
                .build();
    }

}
