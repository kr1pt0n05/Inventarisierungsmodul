package com.hs_esslingen.insy.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCreateDTO {
    private Integer item_id;
    private String item_name;
    private BigDecimal item_price_per_unit;
}
