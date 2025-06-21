package com.hs_esslingen.insy.mapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.hs_esslingen.insy.dto.InventoriesResponseDTO;
import com.hs_esslingen.insy.dto.InventoryCreateRequestDTO;
import com.hs_esslingen.insy.model.Company;
import com.hs_esslingen.insy.model.CostCenter;
import com.hs_esslingen.insy.model.Inventory;
import com.hs_esslingen.insy.model.Tag;
import com.hs_esslingen.insy.model.User;

/**
 * Mapper interface for converting between Inventory entities and DTOs.
 * It uses MapStruct to generate the implementation at compile time.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { TagMapper.class })
public interface InventoryMapper {

    @Mapping(target = "company", source = "company", qualifiedByName = "mapCompanyFromName")
    @Mapping(target = "costCenter", source = "costCenter", qualifiedByName = "mapCostCenterFromName")
    @Mapping(target = "user", source = "orderer", qualifiedByName = "mapUserDynamic")
    @Mapping(target = "tags", expression = "java(mapTags(dto.getTags()))")
    Inventory toEntity(InventoryCreateRequestDTO dto);

    @Mapping(target = "company", source = "company.name")
    @Mapping(target = "costCenter", source = "costCenter.description")
    @Mapping(target = "orderer", source = "user.name")
    @Mapping(target = "tags", source = "tags")
    InventoriesResponseDTO toDto(Inventory entity);

    // Helper methods with @Named so that MapStruct can use them specifically
    @Named("mapCompanyFromName")
    default Company mapCompany(String companyName) {
        if (companyName == null)
            return null;
        return new Company(companyName);
    }

    @Named("mapCostCenterFromName")
    default CostCenter mapCostCenter(String costCenterName) {
        if (costCenterName == null)
            return null;
        return new CostCenter(costCenterName);
    }

    @Named("mapUserDynamic")
    default User mapUserDynamic(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Integer id) {
            User user = new User();
            user.setId(id);
            return user;
        } else if (value instanceof String name) {
            return new User(name);
        } else {
            throw new IllegalArgumentException("Unsupported orderer type: " + value.getClass());
        }
    }

    default LocalDateTime mapCreatedAt(LocalDateTime createdAt) {
        return createdAt;
    }

    // Map the Tag-IDs to Tag-Entities
    default Set<Tag> mapTags(List<Integer> tagIds) {
        if (tagIds == null)
            return Collections.emptySet();
        return tagIds.stream().map(id -> {
            Tag tag = new Tag();
            tag.setId(id);
            return tag;
        }).collect(Collectors.toSet());
    }
}
