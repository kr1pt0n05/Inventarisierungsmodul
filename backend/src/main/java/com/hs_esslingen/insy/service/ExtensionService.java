package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.Optional;

import com.hs_esslingen.insy.exception.NotFoundException;
import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.dto.ExtensionCreateDTO;
import com.hs_esslingen.insy.dto.ExtensionResponseDTO;
import com.hs_esslingen.insy.exception.BadRequestException;
import com.hs_esslingen.insy.mapper.ExtensionMapper;
import com.hs_esslingen.insy.model.Company;
import com.hs_esslingen.insy.model.Extension;
import com.hs_esslingen.insy.model.Inventory;
import com.hs_esslingen.insy.repository.CompanyRepository;
import com.hs_esslingen.insy.repository.ExtensionRepository;
import com.hs_esslingen.insy.repository.InventoryRepository;

@Service
public class ExtensionService {

    private final ExtensionRepository extensionRepository;
    private final InventoryRepository inventoryRepository;
    private final CompanyRepository companyRepository;
    private final ExtensionMapper extensionMapper;

    public ExtensionService(
            ExtensionRepository extensionsRepository,
            InventoryRepository inventoriesRepository,
            CompanyRepository companiesRepository,
            ExtensionMapper extensionsMapper) {

        this.extensionRepository = extensionsRepository;
        this.inventoryRepository = inventoriesRepository;
        this.companyRepository = companiesRepository;
        this.extensionMapper = extensionsMapper;
    }

    /**
     * Retrieves all extensions for a given inventory.
     *
     * @param inventoryId the ID of the inventory
     * @return a list of ExtensionsResponseDTO containing all extensions
     */
    public List<ExtensionResponseDTO> getAllExtensions(Integer inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new NotFoundException("Inventory not found with id: " + inventoryId));
      
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
                .orElseThrow(() -> new NotFoundException("Inventory not found with id: " + inventoryId));

        Extension extension = extensionMapper.toEntity(dto);

        // Muss extra gesetzt werden, da nicht in DTO enthalten
        extension.setInventory(inventory);

        if (dto.getCompanyName() != null) {
            Company company = companyRepository
                    .findByName(dto.getCompanyName())
                    .orElseGet(() -> companyRepository.save(new Company(dto.getCompanyName())));
            extension.setCompany(company);
            company.addExtension(extension);
        }

        inventory.addExtension(extension);

        inventoryRepository.save(inventory);

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

        // Suche das Inventar anhand der ID, wirft Exception, wenn nicht gefunden
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Inventory not found with id: " + id));

        // Suche die Extension anhand der ID im Inventar
        // Optional wird verwendet, um zu vermeiden, dass eine NullPointerException
        // geworfen wird, wenn die Extension nicht gefunden wird
        Optional<Extension> extensionOpt = inventory.getExtensions().stream()
                .filter(ext -> ext.getId().equals(componentId))
                .findFirst();

        // Wenn die Extension gefunden wurde, wird sie in ein DTO umgewandelt und
        if (extensionOpt.isPresent()) {
            return extensionMapper.toDto(extensionOpt.get());
        } else {
            throw new NotFoundException("Extension not found with id: " + componentId);
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

        // Wenn CompanyName in Patch-Daten vorhanden ist und nicht mit der aktuellen
        // Company übereinstimmt
        if (patchData.getCompanyName() != null
                && !patchData.getCompanyName().equals(extension.getCompany().getName())) {

            // Suche nach der Company anhand des Namens
            // Wenn die Company nicht existiert, wird sie neu erstellt
            Company company = companyRepository
                    .findByName(patchData.getCompanyName())
                    .orElseGet(() -> companyRepository.save(new Company(patchData.getCompanyName())));

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
            Inventory inventory = extension.getInventory();
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
            Inventory oldInventory = extension.getInventory();
            oldInventory.removeExtension(extension);

            // Finde das neue Inventar
            Inventory newInventory = inventoryRepository.findById(patchData.getInventoryId())
                    .orElseThrow(
                            () -> new NotFoundException("Inventory not found with id: " + patchData.getInventoryId()));

            // Füge die Extension zum neuen Inventar hinzu
            newInventory.addExtension(extension);
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

        // Entferne die Extension aus dem Inventar
        inventory.removeExtension(extension);

        // Lösche die Extension aus der Datenbank
        extensionRepository.delete(extension);
    }
}
