package com.hs_esslingen.insy.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class InventoryExcel {
    private String costCenter;
    private Integer inventoryNumber;
    private String description;
    private String company;
    private Double price;
    private LocalDateTime createdAt;
    private String serialNumber;
    private String location;
    private String orderer;
    private List<String> comments = new ArrayList<>();
    private boolean isDeinventoried = false;

    public void addComment(String comment) {
        if (comment != null)
            comments.add(comment);
    }
}
