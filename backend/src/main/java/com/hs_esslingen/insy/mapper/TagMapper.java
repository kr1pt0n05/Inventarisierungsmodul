package com.hs_esslingen.insy.mapper;

import com.hs_esslingen.insy.dto.TagDTO;
import com.hs_esslingen.insy.model.Tag;

import org.mapstruct.Mapper;



@Mapper(componentModel = "spring")
public interface TagMapper {

    TagDTO toDto(Tag tag);
}
