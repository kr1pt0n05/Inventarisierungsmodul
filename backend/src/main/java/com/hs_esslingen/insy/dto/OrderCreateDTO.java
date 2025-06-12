package com.hs_esslingen.insy.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateDTO {
    private Integer order_id;
    private LocalDateTime order_created_date;
    private String supplier_name;
    private String cost_center_name;
    private String user_name;
    private BigDecimal order_quote_price;
    private List<ItemCreateDTO> items;
}
