package com.hs_esslingen.insy.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class HistoryResponseDTO {

    private Integer id;

    private String changedBy;

    private String attributeChanged;

    private String valueFrom;

    private String valueTo;

    private LocalDateTime createdAt;
}
