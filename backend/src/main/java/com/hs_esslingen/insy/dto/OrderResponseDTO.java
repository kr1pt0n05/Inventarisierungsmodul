package com.hs_esslingen.insy.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OrderResponseDTO {

    private Integer id;

    private String description;

    private BigDecimal price;

    private String company;

    @JsonProperty("deleted_at")
    private LocalDateTime deletedAt;

    @JsonProperty("order_date")
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;

    @JsonProperty("orderer")
    private String user;

    private List<ArticleDTO> articles;
}
