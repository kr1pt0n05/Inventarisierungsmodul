package com.hs_esslingen.insy.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.dto.SerialNumberDTO;
import com.hs_esslingen.insy.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SerialNumberService {

    private final InventoryRepository inventoryRepository;

    /**
     * Retrieves all unique serial numbers from the inventory repository and returns
     * them as a SerialNumberDTO.
     *
     * @return a SerialNumberDTO containing a set of all unique serial numbers
     */
    public SerialNumberDTO getAllSerialNumbers() {
        Set<String> serialNumbers = inventoryRepository.findAllSerialNumbers();

        return SerialNumberDTO.builder()
                .serialNumbers(serialNumbers)
                .build();
    }
}