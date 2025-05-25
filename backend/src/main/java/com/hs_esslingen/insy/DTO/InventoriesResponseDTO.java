package com.hs_esslingen.insy.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoriesResponseDTO {

    private Integer id;

    private String description;

    private String serialNumber;

    private BigDecimal price;

    private String location;

    private String costCenter;
    
    private String company;

    private String orderer;

    private Boolean isDeinventoried;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;

    private List<TagDTO> tags;
}
