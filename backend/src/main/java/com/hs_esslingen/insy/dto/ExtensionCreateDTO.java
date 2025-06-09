package com.hs_esslingen.insy.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ExtensionCreateDTO {

    @JsonProperty("inventory_id")
    private Integer inventoryId;

    @JsonProperty("description")
    private String description;

    @JsonProperty("serial_number")
    private String serialNumber;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("company_name")
    private String companyName;
}
