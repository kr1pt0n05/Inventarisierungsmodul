package com.hs_esslingen.insy.mapper;

import com.hs_esslingen.insy.dto.ExtensionsCreateDTO;
import com.hs_esslingen.insy.dto.ExtensionsResponseDTO;
import com.hs_esslingen.insy.model.Companies;
import com.hs_esslingen.insy.model.Extensions;
import com.hs_esslingen.insy.model.Inventories;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExtensionsMapper {

    @Mapping(target = "inventory", source = "inventoryId", qualifiedByName = "mapInventoryFromId")
    @Mapping(target = "company", source = "companyName", qualifiedByName = "mapCompanyFromName")
    Extensions toEntity(ExtensionsCreateDTO dto);

    @Mapping(target = "company", source = "company.name")
    @Mapping(target = "created_at", source = "createdAt")
    @Mapping(target = "serial_number", source = "serialNumber")
    ExtensionsResponseDTO toDto(Extensions entity);

    @Named("mapInventoryFromId")
    default Inventories mapInventoryFromId(Integer id) {
        if (id == null) return null;
        Inventories inventory = new Inventories();
        inventory.setId(id);
        return inventory;
    }

    @Named("mapCompanyFromName")
    default Companies mapCompanyFromName(String name) {
        if (name == null) return null;
        return new Companies(name);
    }
}
