package com.hs_esslingen.insy.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ExtensionResponseDTO {
    private Integer id;

    private String description;

    private String company;

    private BigDecimal price;

    @JsonProperty("serial_number")
    private String serialNumber;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;
}
