package com.hs_esslingen.insy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.hs_esslingen.insy.dto.ArticleDTO;
import com.hs_esslingen.insy.model.Article;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleMapper {

    @Mapping(source = "orderer", target = "user")
    Article toEntity(ArticleDTO dto);

    @Mapping(source = "user", target = "orderer")
    ArticleDTO toDto(Article entity);
}
