package com.hs_esslingen.insy.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticNameDTO {
    
    private String name;
    private Integer quantity;
    private BigDecimal orderPrice;
    
    public StatisticNameDTO(String name, Long quantity, BigDecimal orderPrice) {
        this.name = name;
        this.quantity = quantity != null ? quantity.intValue() : 0;
        this.orderPrice = orderPrice != null ? orderPrice : BigDecimal.ZERO;
    }
}