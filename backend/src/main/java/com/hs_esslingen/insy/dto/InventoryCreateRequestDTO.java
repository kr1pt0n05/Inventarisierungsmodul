package com.hs_esslingen.insy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
public class InventoryCreateRequestDTO {

    @JsonProperty("cost_center")
    private String costCenter;


    @JsonProperty("id")
    private Integer inventoriesId;

    private String description;

    private String company;

    private BigDecimal price;

    @JsonProperty("serial_number")
    private String serialNumber;

    private String location;

    @JsonProperty("orderer")
    private Object orderer; // "orderer" needs to be int **or** String

    @JsonProperty("tags")
    private List<Integer> tags;
}
