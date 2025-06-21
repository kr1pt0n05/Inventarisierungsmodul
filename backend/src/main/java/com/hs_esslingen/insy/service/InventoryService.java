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
import com.hs_esslingen.insy.exception.NotFoundException;
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
import com.hs_esslingen.insy.utils.StringParser;

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
     * Retrieves all inventory items based on the provided filters and pagination.
     *
     * @param tags            List of tag IDs to filter by
     * @param minId           Minimum ID for filtering
     * @param maxId           Maximum ID for filtering
     * @param minPrice        Minimum price for filtering
     * @param maxPrice        Maximum price for filtering
     * @param isDeinventoried Filter by deinventoried status
     * @param orderers        List of orderer IDs to filter by
     * @param companies       List of company names to filter by
     * @param locations       List of locations to filter by
     * @param costCenters     List of cost center descriptions to filter by
     * @param serialNumbers   List of serial numbers to filter by
     * @param createdAfter    Filter for creation date after this date
     * @param createdBefore   Filter for creation date before this date
     * @param orderBy         Field to order results by
     * @param direction       Direction of sorting (asc/desc)
     * @param searchText      Text to search in inventory items
     * @param pageable        Pagination information
     * @return Page containing filtered and sorted inventory items as DTOs
     */
    public Page<InventoriesResponseDTO> getAllInventories(
            List<Integer> tags,
            Integer minId,
            Integer maxId,
            Integer minPrice,
            Integer maxPrice,
            Boolean isDeinventoried,
            List<String> orderers,
            List<String> companies,
            List<String> locations,
            List<String> costCenters,
            List<String> serialNumbers,
            LocalDate createdAfter,
            LocalDate createdBefore,
            String orderBy,
            String direction,
            String searchText,
            Pageable pageable) {

        // Convert LocalDate query parameters to LocalDateTime
        // to make filtering work with the database

        // Set start time to 00:00 to include all entries from this date
        LocalDateTime createdAfterTime = createdAfter != null ? createdAfter.atStartOfDay() : null;
        // Set end time to 23:59:59 to include all entries until this date
        LocalDateTime createdBeforeTime = createdBefore != null ? createdBefore.plusDays(1).atStartOfDay().minusNanos(1)
                : null;

        /*
         * Creates SQL-like query with the following form:
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
                .and(InventorySpecification.hasOrderers(orderers))
                .and(InventorySpecification.hasCompanies(companies))
                .and(InventorySpecification.hasLocations(locations))
                .and(InventorySpecification.hasCostCenters(costCenters))
                .and(InventorySpecification.hasSerialNumbers(serialNumbers))
                .and(InventorySpecification.createdBetween(createdAfterTime, createdBeforeTime))
                .and(InventorySpecification.hasSearchText(searchText));

        // Create sorting
        if (orderBy != null && !orderBy.isEmpty()) {
            // Check if the orderBy field is allowed
            if (!OrderByUtils.ALLOWED_ORDER_BY_FIELDS.contains(orderBy)) {
                throw new BadRequestException("Invalid orderBy-field: " + orderBy);
            }

            // Set the default sort direction to ascending
            Sort.Direction sortDirection = Sort.Direction.ASC;
            // If direction is "desc", change to descending
            if ("desc".equalsIgnoreCase(direction)) {
                sortDirection = Sort.Direction.DESC;
            }

            // Check if orderBy is a nested field (e.g. "user.name")
            // If it is nested the sorting needs to be adjusted via Specification
            if (OrderByUtils.FOREIGN_SET.contains(orderBy)) {
                // Sorting via nested fields is NOT to be set in Pageable
                // but via a customized Specification - see below
                spec = spec.and(
                        InventorySpecification.sortByNestedField(orderBy, Sort.Direction.fromString(direction)));

                // If it is not nested then it can be set directly in Pageable
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
     * @throws BadRequestException if the inventory ID already exists
     */
    public InventoriesResponseDTO addInventory(InventoryCreateRequestDTO dto) {
        Inventory inventory = new Inventory();

        // Check if an inventory with this ID already exists
        if (inventoryRepository.existsById(dto.getInventoriesId())) {
            // If it exists throw an exception
            throw new BadRequestException("Inventory with id " + dto.getInventoriesId() + " already exists");
        }
        // Set ID
        inventory.setId(dto.getInventoriesId());

        // Get or create the CostCenter
        CostCenter costCenter = costCenterService.resolveCostCenter(dto.getCostCenter());
        inventory.setCostCenter(costCenter);

        // Get or create the Company
        Company company = companyService.resolveCompany(dto.getCompany());
        inventory.setCompany(company);

        // Get or create the User
        User user = userService.resolveUser(dto.getOrderer());

        // Set remaining fields
        inventory.setDescription(dto.getDescription());
        inventory.setSerialNumber(dto.getSerialNumber());
        inventory.setPrice(dto.getPrice());
        inventory.setLocation(dto.getLocation());
        inventory.setUser(user);

        inventory.setSearchText(StringParser.fullTextSearchString(inventory));

        // Save to allow adding tags to inventory
        inventoryRepository.save(inventory);

        // 6. Link tags

        tagService.addTagsToInventory(inventory.getId(), dto.getTags());

        // Needs to be fetched again because the newly added Tags are not in the DTO
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
            throw new NotFoundException("Inventory with id " + id + " not found.");
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
                case "is_deinventoried":
                    if (fieldValue != null) {
                        inventory.setIsDeinventoried((Boolean) fieldValue);
                    }
                    break;
                default:
                    break;
            }
        }

        Inventory updatedInventory = inventoryRepository.save(inventory);
        inventory.setSearchText(StringParser.fullTextSearchString(inventory));
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
     * creating an update history entry.
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
        // Fields from the Inventory itself
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

        // Extension fields – description + company name
        List<String> extensionFields = inventory.getExtensions().stream()
                .flatMap(ext -> Stream.of(
                        ext.getDescription(),
                        ext.getCompany() != null ? ext.getCompany().getName() : null))
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        // Combine all separated by spaces
        List<String> allFields = new ArrayList<>();
        allFields.addAll(baseFields);
        allFields.addAll(extensionFields);

        // Set the search text for the Inventory
        inventory.setSearchText(String.join("", allFields));
    }

}
