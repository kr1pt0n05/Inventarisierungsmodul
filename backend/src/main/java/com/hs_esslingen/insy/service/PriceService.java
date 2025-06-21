package com.hs_esslingen.insy.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.dto.PriceDTO;
import com.hs_esslingen.insy.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PriceService {
    private final InventoryRepository inventoryRepository;

    /**
     * Retrieves the maximum and minimum prices from the inventory repository and
     * returns them as a PriceDTO.
     *
     * @return a PriceDTO containing the maximum and minimum prices
     */
    public PriceDTO getMaxAndMinPrice() {
        BigDecimal maxPrice = inventoryRepository.findMaxPrice();
        Integer minPrice = inventoryRepository.findMinPrice();

        return PriceDTO.builder()
                .maxPrice((int) Math.ceil(maxPrice.doubleValue()))
                .minPrice(minPrice)
                .build();
    }

}
