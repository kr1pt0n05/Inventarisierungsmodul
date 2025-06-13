package com.hs_esslingen.insy.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.javers.core.Javers;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.configuration.InventorySpecification;
import com.hs_esslingen.insy.dto.InventoriesResponseDTO;
import com.hs_esslingen.insy.dto.InventoryCreateRequestDTO;
import com.hs_esslingen.insy.exception.BadRequestException;
import com.hs_esslingen.insy.mapper.InventoryMapper;
import com.hs_esslingen.insy.model.Company;
import com.hs_esslingen.insy.model.CostCenter;
import com.hs_esslingen.insy.model.History;
import com.hs_esslingen.insy.model.Inventory;
import com.hs_esslingen.insy.model.User;
import com.hs_esslingen.insy.repository.HistoryRepository;
import com.hs_esslingen.insy.repository.InventoryRepository;
import com.hs_esslingen.insy.utils.OrderByUtils;
import com.hs_esslingen.insy.utils.RelationUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final CompanyService companyService;
    private final TagService tagService;
    private final InventoryMapper inventoriesMapper;
    private final OrdererService userService;
    private final CostCenterService costCenterService;
    private final HistoryRepository historyRepository;
    private final Javers javers;

    /**
     * Retrieves an inventory item by its ID.
     *
     * @param id the ID of the inventory item
     * @return ResponseEntity containing the inventory item if found,
     *         or a 404 Not Found status if the item does not exist.
     */
    public ResponseEntity<InventoriesResponseDTO> getInventoryById(Integer id) {
        Optional<Inventory> inventory = inventoryRepository.findById(id);
        if (inventory.isPresent()) {
            InventoriesResponseDTO responseDTO = inventoriesMapper.toDto(inventory.get());
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves all inventory items based on the provided filters.
     *
     * @param tags            List of tag IDs to filter by
     * @param minId           Minimum ID for filtering
     * @param maxId           Maximum ID for filtering
     * @param minPrice        Minimum price for filtering
     * @param maxPrice        Maximum price for filtering
     * @param isDeinventoried Whether to filter by deinventoried status
     * @param orderer         Name of the orderer to filter by
     * @param company         Name of the company to filter by
     * @param location        Location to filter by
     * @param costCenter      Cost center to filter by
     * @param serialNumber    Serial number to filter by
     * @param orderBy         Field to order results by
     * @param direction       Direction of ordering (asc/desc)
     * @param pageable        Pagination information
     * @return Page of InventoriesResponseDTO containing the filtered inventory
     *         items
     */
    public Page<InventoriesResponseDTO> getAllInventories(
            List<Integer> tags,
            Integer minId,
            Integer maxId,
            Integer minPrice,
            Integer maxPrice,
            Boolean isDeinventoried,
            String orderer,
            String company,
            String location,
            String costCenter,
            String serialNumber,
            LocalDate createdAfter,
            LocalDate createdBefore,
            String orderBy,
            String direction,
            String searchText,
            Pageable pageable) {

        // Umwandlung der Query-Parameter von LocalDate in LocalDateTime
        // Damit die Filterung auf die Datenbank funktioniert

        // Setzt den Startzeitpunkt auf 00:00 Uhr, um alle Einträge ab diesem Datum zu
        // berücksichtigen
        LocalDateTime createdAfterTime = createdAfter != null ? createdAfter.atStartOfDay() : null;
        // Setzt den Endzeipunkt auf 23:59:59, um alle Einträge bis zu diesem Datum zu
        // berücksichtigen
        LocalDateTime createdBeforeTime = createdBefore != null ? createdBefore.plusDays(1).atStartOfDay().minusNanos(1)
                : null;

        /*
         * Erstellt SQL-Statement im Stil:
         * SELECT * FROM inventories
         * WHERE
         * tag_id IN (...)
         * AND id BETWEEN ...
         * AND price BETWEEN ...
         * AND ...
         */
        Specification<Inventory> spec = Specification
                .where(InventorySpecification.hasTagId(tags))
                .and(InventorySpecification.idBetween(minId, maxId))
                .and(InventorySpecification.priceBetween(minPrice, maxPrice))
                .and(InventorySpecification.isDeinventoried(isDeinventoried))
                .and(InventorySpecification.hasOrderer(orderer))
                .and(InventorySpecification.hasCompany(company))
                .and(InventorySpecification.hasLocation(location))
                .and(InventorySpecification.hasCostCenter(costCenter))
                .and(InventorySpecification.hasSerialNumber(serialNumber))
                .and(InventorySpecification.createdBetween(createdAfterTime, createdBeforeTime))
                .and(InventorySpecification.hasSearchText(searchText));

        // Sortierung erstellen
        if (orderBy != null && !orderBy.isEmpty()) {
            // Überprüfen, ob das orderBy-Feld erlaubt ist
            if (!OrderByUtils.ALLOWED_ORDER_BY_FIELDS.contains(orderBy)) {
                throw new BadRequestException("Invalid orderBy-field: " + orderBy);
            }

            // Standardmäßig auf aufsteigende Sortierung setzen
            Sort.Direction sortDirection = Sort.Direction.ASC;
            // Wenn die Richtung "desc" ist, dann auf absteigende Sortierung setzen
            if ("desc".equalsIgnoreCase(direction)) {
                sortDirection = Sort.Direction.DESC;
            }

            // Überprüfen, ob das orderBy-Feld verschachtelt ist (z. B. "user.name")
            // Wenn es verschachtelt ist, dann muss die Sortierung über eine angepasste
            // Specification gemacht werden
            if (OrderByUtils.FOREIGN_SET.contains(orderBy)) {
                // Sortierung über verschachtelte Felder wird NICHT im Pageable gesetzt,
                // sondern muss über eine angepasste Specification gemacht werden – siehe unten.
                spec = spec.and(
                        InventorySpecification.sortByNestedField(orderBy, Sort.Direction.fromString(direction)));

                // Wenn es nicht verschachtelt ist, dann kann die Sortierung direkt im Pageable
                // gesetzt werden
            } else {
                Sort sort = Sort.by(sortDirection, orderBy);
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
            }
        }

        Page<Inventory> page = inventoryRepository.findAll(spec, pageable);

        return page.map(inventoriesMapper::toDto);
    }

    /**
     * Adds a new inventory item.
     *
     * @param dto the DTO containing the inventory item data
     * @return ResponseEntity containing the created inventory item
     * @throws IllegalArgumentException if the inventory ID already exists
     */
    public InventoriesResponseDTO addInventory(InventoryCreateRequestDTO dto) {
        Inventory inventory = new Inventory();

        // Existiert ein Inventar mit der Inventarnummer bereits?
        if (inventoryRepository.existsById(dto.getInventoriesId())) {
            // Wenn ja dann eine Exception werfen
            throw new BadRequestException("Inventory with id " + dto.getInventoriesId() + " already exists");
        }
        // ID setzen
        inventory.setId(dto.getInventoriesId());

        // CostCenter holen oder erstellen
        CostCenter costCenter = costCenterService.resolveCostCenter(dto.getCostCenter());
        inventory.setCostCenter(costCenter);

        // Company holen oder erstellen
        Company company = companyService.resolveCompany(dto.getCompany());
        inventory.setCompany(company);

        // User holen oder erstellen
        User user = userService.resolveUser(dto.getOrderer());

        // Restliche Felder setzen
        inventory.setDescription(dto.getDescription());
        inventory.setSerialNumber(dto.getSerialNumber());
        inventory.setPrice(dto.getPrice());
        inventory.setLocation(dto.getLocation());
        inventory.setUser(user);

        changeFullTextSearchString(inventory);

        // saven um Tags auch das inventory hinzufügen zu können
        inventoryRepository.save(inventory);

        // 6. Tags verknüpfen

        tagService.addTagsToInventory(inventory.getId(), dto.getTags());

        // Muss nochmal gefetched werden, da die neu hinzugefügten Tags sonst nicht
        // im DTO enthalten sind
        Inventory updatedInventory = inventoryRepository.findById(inventory.getId()).orElseThrow();

        return inventoriesMapper.toDto(updatedInventory);
    }

    /**
     * Deletes an inventory item by its ID.
     * If the item does not exist, returns a 404 Not Found status.
     * 
     * @param id
     * @return ResponseEntity with no content if the item was deleted,
     *         or a 404 Not Found status if the item does not exist.
     */
    public ResponseEntity<Void> deleteInventory(Integer id) {
        Optional<Inventory> inventory = inventoryRepository.findById(id);
        if (inventory.isPresent()) {
            inventoryRepository.delete(inventory.get());
            return ResponseEntity.noContent().build();
        } else {
            throw new BadRequestException("Inventory with id " + id + " not found.");
        }
    }

    /**
     * Updates an existing inventory item with the provided patch data.
     * The patch data is a map where the keys are field names and the values are the
     * new values for those fields.
     * 
     * @param id
     * @param patchData
     * @return ResponseEntity of type InventoriesResponseDTO containing the updated
     *         inventory item,
     *         or a 404 Not Found status if the item does not exist.
     */
    public ResponseEntity<InventoriesResponseDTO> updateInventory(Integer id, Map<String, Object> patchData) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(id);
        if (inventoryOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Inventory inventory = inventoryOptional.get();

        // Inventory before change
        InventoryCreateRequestDTO inventoryOld = InventoryService.mapInventoryToDto(inventory);

        for (Map.Entry<String, Object> entry : patchData.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();

            switch (fieldName) {
                case "cost_center":
                    if (fieldValue != null) {
                        CostCenter newCostCenter = costCenterService.resolveCostCenter(fieldValue);
                        CostCenter oldCostCenter = inventory.getCostCenter();
                        RelationUtils.switchRelation(
                                oldCostCenter != null ? oldCostCenter.getInventories() : null,
                                newCostCenter != null ? newCostCenter.getInventories() : null,
                                inventory);
                        inventory.setCostCenter(newCostCenter);
                    }
                    break;
                case "description":
                    if (fieldValue != null) {
                        inventory.setDescription((String) fieldValue);
                    }
                    break;
                case "company":
                    if (fieldValue != null) {
                        Company newCompany = companyService.resolveCompany(fieldValue);
                        Company oldCompany = inventory.getCompany();
                        RelationUtils.switchRelation(
                                oldCompany != null ? oldCompany.getInventories() : null,
                                newCompany != null ? newCompany.getInventories() : null,
                                inventory);
                        inventory.setCompany(newCompany);
                    }
                    break;
                case "price":
                    if (fieldValue != null) {
                        inventory.setPrice(new BigDecimal(fieldValue.toString()));
                    }
                    break;
                case "serial_number":
                    if (fieldValue != null) {
                        inventory.setSerialNumber((String) fieldValue);
                    }
                    break;
                case "location":
                    if (fieldValue != null) {
                        inventory.setLocation((String) fieldValue);
                    }
                    break;
                case "orderer":
                    if (fieldValue != null) {
                        User newUser = userService.resolveUser(fieldValue);
                        User oldUser = inventory.getUser();
                        RelationUtils.switchRelation(
                                oldUser != null ? oldUser.getInventories() : null,
                                newUser != null ? newUser.getInventories() : null,
                                inventory);
                        inventory.setUser(newUser);
                    }
                    break;
                default:
                    break;
            }
        }

        Inventory updatedInventory = inventoryRepository.save(inventory);
        changeFullTextSearchString(updatedInventory);
        inventoryRepository.save(updatedInventory);

        // Inventory after change
        InventoryCreateRequestDTO inventoryNew = InventoryService.mapInventoryToDto(updatedInventory);

        // Store the changes to History entity
        List<History> historyList = new ArrayList<>();
        Diff diff = javers.compare(inventoryOld, inventoryNew);

        diff.getChangesByType(ValueChange.class).forEach(change -> {
            String property = change.getPropertyName();
            Object before = change.getLeft();
            Object after = change.getRight();

            History history = new History();
            history.setAuthor(inventory.getUser()); // Replace this with user from JWT token
            history.setAttributeChanged(property);
            history.setValueFrom(before == null ? "null" : before.toString());
            history.setValueTo(after.toString());
            history.setInventory(inventory);
            historyList.add(history);
        });
        historyRepository.saveAll(historyList);

        InventoriesResponseDTO responseDTO = inventoriesMapper.toDto(updatedInventory);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Maps an Inventory object to an InventoryCreateRequestDTO.
     * This method is used to convert an Inventory entity to a DTO for
     * creating a update history entry.
     *
     * @param inventory the Inventory entity to map
     * @return the mapped InventoryCreateRequestDTO
     */
    private static InventoryCreateRequestDTO mapInventoryToDto(Inventory inventory) {
        InventoryCreateRequestDTO dto = new InventoryCreateRequestDTO();
        dto.setInventoriesId(inventory.getId());
        dto.setDescription(inventory.getDescription() == null ? null : inventory.getDescription().toString());
        dto.setSerialNumber(inventory.getSerialNumber() == null ? null : inventory.getSerialNumber().toString());
        dto.setPrice(inventory.getPrice());
        dto.setLocation(inventory.getLocation() == null ? null : inventory.getLocation().toString());
        dto.setCostCenter(inventory.getCostCenter() == null ? null : inventory.getCostCenter().getDescription());
        dto.setCompany(inventory.getCompany() == null ? null : inventory.getCompany().getName());
        dto.setOrderer(inventory.getUser() == null ? null : inventory.getUser().getId());
        return dto;
    }

    /**
     * Generates a full-text search string for an inventory item.
     * This string is used for searching across multiple fields in the inventory.
     *
     * @param inventory the inventory item
     * @return a string containing all searchable fields, concatenated and separated
     *         by spaces, and lowercased
     */
    public void changeFullTextSearchString(Inventory inventory) {
        // Felder des Inventory selbst
        List<String> baseFields = Stream.of(
                inventory.getDescription(),
                inventory.getSerialNumber(),
                inventory.getLocation(),
                inventory.getCompany() != null ? inventory.getCompany().getName() : null,
                inventory.getCostCenter() != null ? inventory.getCostCenter().getDescription() : null,
                inventory.getUser() != null ? inventory.getUser().getName() : null)
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        // Erweiterungen (Extensions) – Beschreibung + Company-Name
        List<String> extensionFields = inventory.getExtensions().stream()
                .flatMap(ext -> Stream.of(
                        ext.getDescription(),
                        ext.getCompany() != null ? ext.getCompany().getName() : null))
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        // Alle zusammenfügen, getrennt durch Leerzeichen
        List<String> allFields = new ArrayList<>();
        allFields.addAll(baseFields);
        allFields.addAll(extensionFields);

        // Setzen des Suchtextes für das Inventory
        inventory.setSearchText(String.join(" ", allFields));
    }

}
