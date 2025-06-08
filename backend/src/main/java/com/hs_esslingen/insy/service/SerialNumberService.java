package com.hs_esslingen.insy.service;

import java.util.Set;
import com.hs_esslingen.insy.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class SerialNumberService {

    private final InventoryRepository inventoryRepository;

    // Get Serial numbers from repository
    public Set<String> getAllSerialNumbers() {
        return inventoryRepository.findAllSerialNumbers();
    }
}