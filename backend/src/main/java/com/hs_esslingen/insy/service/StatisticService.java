package com.hs_esslingen.insy.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hs_esslingen.insy.dto.StatisticDTO;
import com.hs_esslingen.insy.dto.StatisticNameDTO;
import com.hs_esslingen.insy.exception.InternalServerErrorException;
import com.hs_esslingen.insy.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticService {
    
    private final OrderRepository orderRepository;
    
    @Transactional(readOnly = true)
    public List<StatisticDTO> getOrderStatistics() {
        try {
            
            // Fetching total orders and total price
            Long totalOrders = orderRepository.countActiveOrders();
            BigDecimal totalPrice = orderRepository.sumActivePrices();
            
            // Fetching user statistics
            List<Object[]> userStats = orderRepository.findOrderStatisticsByUser();
            
            List<StatisticNameDTO> statisticNames = userStats.stream()
                    .map(row -> new StatisticNameDTO(
                            (String) row[0],        // user name
                            (Long) row[1],          // quantity
                            (BigDecimal) row[2]     // order price
                    ))
                    .collect(Collectors.toList());
                    
            // Creating main statistic
            StatisticDTO mainStatistic = StatisticDTO.builder()
                    .totalOrders(totalOrders != null ? totalOrders.intValue() : 0)
                    .totalPrice(totalPrice != null ? totalPrice : BigDecimal.ZERO)
                    .names(statisticNames)
                    .build();
                      
            return List.of(mainStatistic);
            
        } catch (Exception e) {
            throw new InternalServerErrorException("Fehler beim Abrufen der Bestellstatistiken: " + e.getMessage());
        }
    }
}