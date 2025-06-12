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
public class PriceDTO {
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
}
