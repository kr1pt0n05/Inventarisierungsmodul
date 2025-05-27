package com.hs_esslingen.insy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@Getter
@Setter
public class TagDTO {

    public Integer id;
    public String name;


    @Builder
    public TagDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
