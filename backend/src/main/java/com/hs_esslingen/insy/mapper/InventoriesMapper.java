package com.hs_esslingen.insy.mapper;

import com.hs_esslingen.insy.dto.InventoriesCreateRequestDTO;
import com.hs_esslingen.insy.dto.InventoriesResponseDTO;
import com.hs_esslingen.insy.model.Companies;
import com.hs_esslingen.insy.model.CostCenters;
import com.hs_esslingen.insy.model.Inventories;
import com.hs_esslingen.insy.model.Tags;
import com.hs_esslingen.insy.model.Users;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { TagMapper.class })
public interface InventoriesMapper {

    @Mapping(target = "company", source = "company", qualifiedByName = "mapCompanyFromName")
    @Mapping(target = "costCenter", source = "costCenter", qualifiedByName = "mapCostCenterFromName")
    @Mapping(target = "user", source = "orderer", qualifiedByName = "mapUserDynamic")
    @Mapping(target = "tags", expression = "java(mapTags(dto.getTags()))")
    Inventories toEntity(InventoriesCreateRequestDTO dto);

    @Mapping(target = "company", source = "company.name")
    @Mapping(target = "costCenter", source = "costCenter.description")
    @Mapping(target = "orderer", source = "user.name")
    @Mapping(target = "tags", source = "tags")
    InventoriesResponseDTO toDto(Inventories entity);

    // Hilfsmethoden mit @Named, damit MapStruct sie gezielt nutzen kann
    @Named("mapCompanyFromName")
    default Companies mapCompany(String companyName) {
        if (companyName == null)
            return null;
        return new Companies(companyName);
    }


    @Named("mapCostCenterFromName")
    default CostCenters mapCostCenter(String costCenterName) {
        if (costCenterName == null)
            return null;
        return new CostCenters(costCenterName);
    }

    @Named("mapUserDynamic")
default Users mapUserDynamic(Object value) {
    if (value == null) {
        return null;
    }
    if (value instanceof Integer id) {
        Users user = new Users();
        user.setId(id);
        return user;
    } else if (value instanceof String name) {
        return new Users(name); // vorausgesetzt du hast den passenden Konstruktor
    } else {
        throw new IllegalArgumentException("Unsupported orderer type: " + value.getClass());
    }
}

    
     default LocalDateTime mapCreatedAt(LocalDateTime createdAt) {
        return createdAt;
    } 

    // Mapping der Tag-IDs zu Tag-Entitäten
    default Set<Tags> mapTags(List<Integer> tagIds) {
        if (tagIds == null)
            return Collections.emptySet();
        return tagIds.stream().map(id -> {
            Tags tag = new Tags();
            tag.setId(id);
            return tag;
        }).collect(Collectors.toSet());
    }
}
