package com.hs_esslingen.insy.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hs_esslingen.insy.dto.StatisticDTO;
import com.hs_esslingen.insy.dto.StatisticNameDTO;
import com.hs_esslingen.insy.exception.InternalServerErrorException;
import com.hs_esslingen.insy.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticService {

    private final InventoryRepository inventoryRepository;


    /**
     * Retrieves order statistics including total orders, total price, and
     * user-specific statistics.
     * 
     * @return a list containing a single StatisticDTO with the aggregated
     *         statistics.
     * @throws InternalServerErrorException if an error occurs while fetching the
     *                                      statistics.
     */
    @Transactional(readOnly = true)
    public List<StatisticDTO> getInventoryStatistics() {
        try {
            Long totalInventories = inventoryRepository.countActiveInventories();
            BigDecimal totalPrice = inventoryRepository.sumActiveInventoryPrices();

            List<Object[]> userStats = inventoryRepository.findInventoryStatisticsByUser();

            List<StatisticNameDTO> statisticNames = userStats.stream()
                .map(row -> new StatisticNameDTO(
                        (String) row[0],        // user name
                        (Long) row[1],          // quantity
                        (BigDecimal) row[2]     // total price
                ))
                .collect(Collectors.toList());

            StatisticDTO mainStatistic = StatisticDTO.builder()
                .totalOrders(totalInventories != null ? totalInventories.intValue() : 0)
                .totalPrice(totalPrice != null ? totalPrice : BigDecimal.ZERO)
                .names(statisticNames)
                .build();

            return List.of(mainStatistic);
        } catch (Exception e) {
            throw new InternalServerErrorException("Fehler beim Abrufen der Inventarstatistiken: " + e.getMessage());
        }
    }
}