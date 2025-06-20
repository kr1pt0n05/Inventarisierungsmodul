package com.hs_esslingen.insy.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticDTO {
    
    private Integer totalOrders;
    private BigDecimal totalPrice;
    private List<StatisticNameDTO> names;
}