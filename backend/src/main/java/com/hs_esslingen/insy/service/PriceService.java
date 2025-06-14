package com.hs_esslingen.insy.service;

import com.hs_esslingen.insy.dto.PriceDTO;
import com.hs_esslingen.insy.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PriceService {
    private final InventoryRepository inventoryRepository;

    public PriceDTO getMaxAndMinPrice() {
        BigDecimal maxPrice = inventoryRepository.findMaxPrice();
        Integer minPrice = inventoryRepository.findMinPrice();

        return PriceDTO.builder()
                .maxPrice((int) Math.ceil(maxPrice.doubleValue()))
                .minPrice(minPrice)
                .build();
    }

}
