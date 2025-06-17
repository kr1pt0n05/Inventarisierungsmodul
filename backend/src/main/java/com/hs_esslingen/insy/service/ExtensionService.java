package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.Optional;

import com.hs_esslingen.insy.exception.NotFoundException;
import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.dto.ExtensionCreateDTO;
import com.hs_esslingen.insy.dto.ExtensionResponseDTO;
import com.hs_esslingen.insy.mapper.ExtensionMapper;
import com.hs_esslingen.insy.model.Company;
import com.hs_esslingen.insy.model.Extension;
import com.hs_esslingen.insy.model.Inventory;
import com.hs_esslingen.insy.repository.CompanyRepository;
import com.hs_esslingen.insy.repository.ExtensionRepository;
import com.hs_esslingen.insy.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExtensionService {

    private final ExtensionRepository extensionRepository;
    private final InventoryService inventoryService;
    private final InventoryRepository inventoryRepository;
    private final CompanyRepository companyRepository;
    private final ExtensionMapper extensionMapper;

    /**
     * Retrieves all extensions for a given inventory.
     *
     * @param inventoryId the ID of the inventory
     * @return a list of ExtensionsResponseDTO containing all extensions
     */
    public List<ExtensionResponseDTO> getAllExtensions(Integer inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new NotFoundException("Inventory with id: " + inventoryId + " not found"));
      
        List<Extension> extensions = inventory.getExtensions();
        return extensions.stream()
                .map(extensionMapper::toDto)
                .toList();
    }

    /**
     * Adds a new extension to the specified inventory.
     *
     * @param inventoryId the ID of the inventory
     * @param dto         the data transfer object containing extension details
     * @return the created ExtensionsResponseDTO
     */
    public ExtensionResponseDTO addExtension(Integer inventoryId, ExtensionCreateDTO dto) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new NotFoundException("Inventory with id: " + inventoryId + " not found"));

        Extension extension = extensionMapper.toEntity(dto);
        extension.setInventory(inventory);

        if (dto.getCompanyName() != null) {
            Company company = companyRepository
                    .findByName(dto.getCompanyName())
                    .orElseGet(() -> companyRepository.save(new Company(dto.getCompanyName())));
            extension.setCompany(company);
        }

        inventory.addExtension(extension);
        inventoryService.changeFullTextSearchString(inventory);
        extensionRepository.save(extension);
        inventoryRepository.flush();

        return extensionMapper.toDto(extension);
    }

    /**
     * Retrieves an extension by its ID from a specific inventory.
     *
     * @param id          the ID of the inventory
     * @param componentId the ID of the extension
     * @return the ExtensionsResponseDTO containing the extension details
     */
    public ExtensionResponseDTO getExtensionById(Integer id, Integer componentId) {

        // Find the inventory by ID, throw exception if not found
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Inventory with id: " + id + " not found"));

        // Search for the Extension by ID
        // Optional is used to avoid NullPointerException if the extension is not found
        Optional<Extension> extensionOpt = inventory.getExtensions().stream()
                .filter(ext -> ext.getId().equals(componentId))
                .findFirst();

        // If the extension was found, map it to a DTO and return it
        if (extensionOpt.isPresent()) {
            return extensionMapper.toDto(extensionOpt.get());
        } else {
            throw new NotFoundException("Extension with id: " + componentId + " not found");
        }
    }

    /**
     * Updates an existing extension in the specified inventory.
     *
     * @param id          the ID of the inventory
     * @param componentId the ID of the extension to update
     * @param patchData   the data transfer object containing updated extension
     *                    details
     * @return the updated ExtensionsResponseDTO
     */
    public ExtensionResponseDTO updateExtension(Integer id, Integer componentId, ExtensionCreateDTO patchData) {

        Extension extension = extensionRepository.findById(componentId)
                .orElseThrow(() -> new NotFoundException("Extension not found with id: " + componentId));

        // If CompanyName is present in the patch data and differs from the current one
        if (patchData.getCompanyName() != null
                && !patchData.getCompanyName().equals(extension.getCompany().getName())) {

            // Search for the company by name
            // If the company doesn't exist, create it
            Company company = companyRepository
                    .findByName(patchData.getCompanyName())
                    .orElseGet(() -> companyRepository.save(new Company(patchData.getCompanyName())));

            extension.setCompany(company);
            company.addExtension(extension);
        }

        // Update the fields present in the patch data
        if (patchData.getDescription() != null) {
            extension.setDescription(patchData.getDescription());
        }

        if (patchData.getSerialNumber() != null) {
            extension.setSerialNumber(patchData.getSerialNumber());
        }

        if (patchData.getPrice() != null) {
            Inventory inventory = extension.getInventory();
            // If price is updated, the inventory's price must be adjusted
            if (inventory.getPrice() == null) {
                inventory.setPrice(patchData.getPrice());
            } else {
                // Subtract the old price of the extension and add the new price
                inventory.setPrice(inventory.getPrice().subtract(extension.getPrice()).add(patchData.getPrice()));
            }
            extension.setPrice(patchData.getPrice());

        }

        // If InventoryId is present in patch data and differs from current inventory
        if (patchData.getInventoryId() != null
                && !patchData.getInventoryId().equals(extension.getInventory().getId())) {

            // Remove the extension from the old inventory
            Inventory oldInventory = extension.getInventory();
            oldInventory.removeExtension(extension);
            inventoryService.changeFullTextSearchString(oldInventory);

            // Find the new inventory
            Inventory newInventory = inventoryRepository.findById(patchData.getInventoryId())
                    .orElseThrow(
                            () -> new NotFoundException("Inventory not found with id: " + patchData.getInventoryId()));

            // Add the extension to the new inventory
            newInventory.addExtension(extension);
            inventoryService.changeFullTextSearchString(newInventory);
        }

        Extension updated = extensionRepository.save(extension);
        return extensionMapper.toDto(updated);
    }

    /**
     * Deletes an extension from a specific inventory.
     *
     * @param id          the ID of the inventory
     * @param componentId the ID of the extension to delete
     */
    public void deleteExtension(Integer id, Integer componentId) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Inventory not found with id: " + id));

        Extension extension = extensionRepository.findById(componentId)
                .orElseThrow(() -> new NotFoundException("Extension not found with id: " + componentId));

        // Remove the Extension from the inventory
        inventory.removeExtension(extension);

        // Remove the Extension from the database
        extensionRepository.delete(extension);
    }
}
