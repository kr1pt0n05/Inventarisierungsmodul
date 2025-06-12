package com.hs_esslingen.insy.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryCreateRequestDTO {

    @JsonProperty("cost_center")
    private String costCenter;

    @JsonProperty("id")
    // @NotNull
    private Integer inventoriesId;

    private String description;

    private String company;

    private BigDecimal price;

    @JsonProperty("serial_number")
    private String serialNumber;

    private String location;

    @JsonProperty("orderer")
    private Object orderer; // weil "orderer" int **oder** String sein kann

    @JsonProperty("tags")
    private List<Integer> tags;
}
