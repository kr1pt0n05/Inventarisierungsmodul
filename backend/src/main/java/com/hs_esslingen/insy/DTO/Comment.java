package com.hs_esslingen.insy.DTO;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Integer id;
    private String description;
    private OffsetDateTime createdAt;
    private String author;
}
