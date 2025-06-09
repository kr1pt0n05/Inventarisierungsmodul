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
    public String costCenter;

    @JsonProperty("inventories_id")
    //@NotNull
    public Integer inventoriesId;

    @JsonProperty("inventories_description")
    public String description;

    @JsonProperty("company")
    public String company;

    @JsonProperty("inventories_price")
    public BigDecimal price;

    @JsonProperty("inventories_serial_number")
    public String serialNumber;

    @JsonProperty("inventories_location")
    public String location;

    @JsonProperty("orderer")
    public Object orderer; // weil "orderer" int **oder** String sein kann

    @JsonProperty("tags")
    public List<Integer> tags;
}
