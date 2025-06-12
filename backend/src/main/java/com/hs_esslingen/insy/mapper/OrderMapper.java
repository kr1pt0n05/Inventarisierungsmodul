package com.hs_esslingen.insy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.hs_esslingen.insy.dto.OrderResponseDTO;
import com.hs_esslingen.insy.model.Order;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { ArticleMapper.class })
public interface OrderMapper {

    @Mapping(target = "articles", source = "articles")
    OrderResponseDTO toDto(Order entity);
}
