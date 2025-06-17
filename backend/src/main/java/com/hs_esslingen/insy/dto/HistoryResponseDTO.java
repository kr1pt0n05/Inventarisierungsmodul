package com.hs_esslingen.insy.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hs_esslingen.insy.model.Inventory;
import com.hs_esslingen.insy.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor @ToString
public class HistoryResponseDTO {

    private Integer id;

    private String changedBy;

    private String attributeChanged;

    private String valueFrom;

    private String valueTo;

    private LocalDateTime createdAt;
}
