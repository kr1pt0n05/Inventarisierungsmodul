package com.hs_esslingen.insy.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ArticleDTO {

    @JsonProperty("article_id")
    private Integer id;

    @JsonProperty("inventories_id")
    private Integer inventoriesId;

    private String description;

    private BigDecimal price;

    private String company;

    @JsonProperty("is_inventoried")
    private Boolean isInventoried;

    @JsonProperty("inventories_serial_number")
    private String serialNumber;

    private String location;

    private String orderer;

    @JsonProperty("is_extension")
    private Boolean isExtension;

    private List<Integer> tags;

}
