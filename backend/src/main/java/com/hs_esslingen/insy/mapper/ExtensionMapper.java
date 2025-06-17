package com.hs_esslingen.insy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.hs_esslingen.insy.dto.ExtensionCreateDTO;
import com.hs_esslingen.insy.dto.ExtensionResponseDTO;
import com.hs_esslingen.insy.model.Extension;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExtensionMapper {

    @Mapping(target = "company", ignore = true)
    Extension toEntity(ExtensionCreateDTO dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "company", source = "company.name")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "serialNumber", source = "serialNumber")
    ExtensionResponseDTO toDto(Extension entity);
}
