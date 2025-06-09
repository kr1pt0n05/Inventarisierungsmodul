package com.hs_esslingen.insy.mapper;

import org.mapstruct.Mapper;

import com.hs_esslingen.insy.dto.TagDTO;
import com.hs_esslingen.insy.model.Tag;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagDTO toDto(Tag tag);
}
