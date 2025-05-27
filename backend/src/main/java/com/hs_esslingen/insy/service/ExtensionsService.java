package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.dto.ExtensionsCreateDTO;
import com.hs_esslingen.insy.dto.ExtensionsResponseDTO;
import com.hs_esslingen.insy.mapper.ExtensionsMapper;
import com.hs_esslingen.insy.model.Companies;
import com.hs_esslingen.insy.model.Extensions;
import com.hs_esslingen.insy.model.Inventories;
import com.hs_esslingen.insy.repository.CompaniesRepository;
import com.hs_esslingen.insy.repository.ExtensionsRepository;
import com.hs_esslingen.insy.repository.InventoriesRepository;

@Service
public class ExtensionsService {

    private final ExtensionsRepository extensionsRepository;
    private final InventoriesRepository inventoriesRepository;
    private final CompaniesRepository companiesRepository;
    private final ExtensionsMapper extensionsMapper;

    public ExtensionsService(
            ExtensionsRepository extensionsRepository,
            InventoriesRepository inventoriesRepository,
            CompaniesRepository companiesRepository,
            ExtensionsMapper extensionsMapper) {

        this.extensionsRepository = extensionsRepository;
        this.inventoriesRepository = inventoriesRepository;
        this.companiesRepository = companiesRepository;
        this.extensionsMapper = extensionsMapper;
    }

    /**
     * Retrieves all extensions for a given inventory.
     *
     * @param inventoryId the ID of the inventory
     * @return a list of ExtensionsResponseDTO containing all extensions
     */
    public List<ExtensionsResponseDTO> getAllExtensions(Integer inventoryId) {
        Inventories inventory = inventoriesRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + inventoryId));
        List<Extensions> extensions = inventory.getExtensions();
        return extensions.stream()
                .map(extensionsMapper::toDto)
                .toList();
    }

    /**
     * Adds a new extension to the specified inventory.
     *
     * @param inventoryId the ID of the inventory
     * @param dto         the data transfer object containing extension details
     * @return the created ExtensionsResponseDTO
     */
    public ExtensionsResponseDTO addExtension(Integer inventoryId, ExtensionsCreateDTO dto) {

        Inventories inventory = inventoriesRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + inventoryId));

        Extensions extension = extensionsMapper.toEntity(dto);

        // Muss extra gesetzt werden, da nicht in DTO enthalten
        extension.setInventory(inventory);

        if (dto.getCompanyName() != null) {
            Companies company = companiesRepository
                    .findByName(dto.getCompanyName())
                    .orElseGet(() -> companiesRepository.save(new Companies(dto.getCompanyName())));
            extension.setCompany(company);
            company.addExtension(extension);
        }

        inventory.addExtension(extension);

        inventoriesRepository.save(inventory);

        return extensionsMapper.toDto(extension);
    }

    /**
     * Retrieves an extension by its ID from a specific inventory.
     *
     * @param id          the ID of the inventory
     * @param componentId the ID of the extension
     * @return the ExtensionsResponseDTO containing the extension details
     */
    public ExtensionsResponseDTO getExtensionById(Integer id, Integer componentId) {

        // Scuhe das Inventar anhand der ID, wirft Exception, wenn nicht gefunden
        Inventories inventory = inventoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));

        // Suche die Extension anhand der ID im Inventar
        // Optional wird verwendet, um zu vermeiden, dass eine NullPointerException
        // geworfen wird, wenn die Extension nicht gefunden wird
        Optional<Extensions> extensionOpt = inventory.getExtensions().stream()
                .filter(ext -> ext.getId().equals(componentId))
                .findFirst();

        // Wenn die Extension gefunden wurde, wird sie in ein DTO umgewandelt und
        if (extensionOpt.isPresent()) {
            return extensionsMapper.toDto(extensionOpt.get());
        } else {
            throw new RuntimeException("Extension not found with id: " + componentId);
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
    public ExtensionsResponseDTO updateExtension(Integer id, Integer componentId, ExtensionsCreateDTO patchData) {

        Extensions extension = extensionsRepository.findById(componentId)
                .orElseThrow(() -> new RuntimeException("Extension not found with id: " + componentId));

        // Wenn CompanyName in Patch-Daten vorhanden ist und nicht mit der aktuellen
        // Company übereinstimmt
        if (patchData.getCompanyName() != null
                && !patchData.getCompanyName().equals(extension.getCompany().getName())) {

            // Suche nach der Company anhand des Namens
            // Wenn die Company nicht existiert, wird sie neu erstellt
            Companies company = companiesRepository
                    .findByName(patchData.getCompanyName())
                    .orElseGet(() -> companiesRepository.save(new Companies(patchData.getCompanyName())));

            extension.setCompany(company);
            company.addExtension(extension);
        }

        // Aktualisiere die Felder, die im Patch-Daten vorhanden sind
        if (patchData.getDescription() != null) {
            extension.setDescription(patchData.getDescription());
        }

        if (patchData.getSerialNumber() != null) {
            extension.setSerialNumber(patchData.getSerialNumber());
        }

        if (patchData.getPrice() != null) {
            Inventories inventory = extension.getInventory();
            // Wenn der Preis aktualisiert wird, muss der Preis des Inventars angepasst
            // werden
            if (inventory.getPrice() == null) {
                inventory.setPrice(patchData.getPrice());
            } else {
                // Subtrahiere den alten Preis der Extension und addiere den neuen Preis
                inventory.setPrice(inventory.getPrice().subtract(extension.getPrice()).add(patchData.getPrice()));
            }
            extension.setPrice(patchData.getPrice());

        }

        // Wenn InventoryId im Patch-Daten vorhanden ist und nicht mit dem aktuellen
        // Inventory übereinstimmt
        if (patchData.getInventoryId() != null
                && !patchData.getInventoryId().equals(extension.getInventory().getId())) {

            // Entferne die Extension aus dem alten Inventar
            Inventories oldInventory = extension.getInventory();
            oldInventory.removeExtension(extension);

            // Finde das neue Inventar
            Inventories newInventory = inventoriesRepository.findById(patchData.getInventoryId())
                    .orElseThrow(
                            () -> new RuntimeException("Inventory not found with id: " + patchData.getInventoryId()));

            // Füge die Extension zum neuen Inventar hinzu
            newInventory.addExtension(extension);
        }

        Extensions updated = extensionsRepository.save(extension);
        return extensionsMapper.toDto(updated);
    }

    /**
     * Deletes an extension from a specific inventory.
     *
     * @param id          the ID of the inventory
     * @param componentId the ID of the extension to delete
     */
    public void deleteExtension(Integer id, Integer componentId) {
        Inventories inventory = inventoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));

        Extensions extension = extensionsRepository.findById(componentId)
                .orElseThrow(() -> new RuntimeException("Extension not found with id: " + componentId));

        // Entferne die Extension aus dem Inventar
        inventory.removeExtension(extension);

        // Lösche die Extension aus der Datenbank
        extensionsRepository.delete(extension);
    }
}
