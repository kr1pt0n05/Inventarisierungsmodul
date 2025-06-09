package com.hs_esslingen.insy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.hs_esslingen.insy.dto.ExtensionCreateDTO;
import com.hs_esslingen.insy.dto.ExtensionResponseDTO;
import com.hs_esslingen.insy.model.Company;
import com.hs_esslingen.insy.model.Extension;
import com.hs_esslingen.insy.model.Inventory;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExtensionMapper {

    @Mapping(target = "inventory", source = "inventoryId", qualifiedByName = "mapInventoryFromId")
    @Mapping(target = "company", source = "companyName", qualifiedByName = "mapCompanyFromName")
    Extension toEntity(ExtensionCreateDTO dto);

    @Mapping(target = "company", source = "company.name")
    @Mapping(target = "created_at", source = "createdAt")
    @Mapping(target = "serial_number", source = "serialNumber")
    ExtensionResponseDTO toDto(Extension entity);

    @Named("mapInventoryFromId")
    default Inventory mapInventoryFromId(Integer id) {
        if (id == null)
            return null;
        Inventory inventory = new Inventory();
        inventory.setId(id);
        return inventory;
    }

    @Named("mapCompanyFromName")
    default Company mapCompanyFromName(String name) {
        if (name == null)
            return null;
        return new Company(name);
    }
}
