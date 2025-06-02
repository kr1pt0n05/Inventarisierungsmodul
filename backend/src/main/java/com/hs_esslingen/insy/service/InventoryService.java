package com.hs_esslingen.insy.service;

import com.hs_esslingen.insy.repository.CompanyRepository;
import com.hs_esslingen.insy.repository.CostCenterRepository;
import com.hs_esslingen.insy.repository.InventoryRepository;
import com.hs_esslingen.insy.repository.TagRepository;
import com.hs_esslingen.insy.repository.UserRepository;
import com.hs_esslingen.insy.utils.OrderByUtils;
import com.hs_esslingen.insy.configuration.InventorySpecification;
import com.hs_esslingen.insy.dto.InventoryCreateRequestDTO;
import com.hs_esslingen.insy.dto.InventoriesResponseDTO;
import com.hs_esslingen.insy.mapper.InventoryMapper;
import com.hs_esslingen.insy.model.CostCenter;
import com.hs_esslingen.insy.model.Inventory;
import com.hs_esslingen.insy.model.Tag;
import com.hs_esslingen.insy.model.Company;
import com.hs_esslingen.insy.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final InventoryRepository inventoriesRepository;
    private final UserRepository usersRepository;
    private final CompanyRepository companiesRepository;
    private final CostCenterRepository costCentersRepository;
    private final TagRepository tagsRepository;
    private final InventoryMapper inventoriesMapper;

    public InventoryService(
            InventoryRepository inventoryRepository,
            UserRepository userRepository,
            CompanyRepository companyRepository,
            CostCenterRepository costCenterRepository,
            TagRepository tagRepository,
            InventoryMapper inventoryMapper) {
        this.inventoriesRepository = inventoryRepository;
        this.usersRepository = userRepository;
        this.companiesRepository = companyRepository;
        this.costCentersRepository = costCenterRepository;
        this.tagsRepository = tagRepository;
        this.inventoriesMapper = inventoryMapper;
    }

    /**
     * Retrieves an inventory item by its ID.
     *
     * @param id the ID of the inventory item
     * @return ResponseEntity containing the inventory item if found,
     *         or a 404 Not Found status if the item does not exist.
     */
    public ResponseEntity<InventoriesResponseDTO> getInventoryById(Integer id) {
        Optional<Inventory> inventory = inventoriesRepository.findById(id);
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
            Pageable pageable) {


        // Umwandlung der Query-Parameter von LocalDate in LocalDateTime
        // Damit die Filterung auf die Datenbank funktioniert

        // Setzt den Startzeitpunkt auf 00:00 Uhr, um alle Einträge ab diesem Datum zu berücksichtigen
        LocalDateTime createdAfterTime = createdAfter != null ? createdAfter.atStartOfDay() : null;
        // Setzt den Endzeipunkt auf 23:59:59, um alle Einträge bis zu diesem Datum zu berücksichtigen
        LocalDateTime createdBeforeTime = createdBefore != null ? createdBefore.plusDays(1).atStartOfDay().minusNanos(1) : null;

        /*  Erstellt SQL-Statement im Stil:
        SELECT * FROM inventories
        WHERE 
            tag_id IN (...)
            AND id BETWEEN ...
            AND price BETWEEN ...
            AND ...*/
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
                .and(InventorySpecification.createdBetween(createdAfterTime, createdBeforeTime));


        // Sortierung erstellen
        if (orderBy != null && !orderBy.isEmpty()) {
            // Überprüfen, ob das orderBy-Feld erlaubt ist
            if (!OrderByUtils.ALLOWED_ORDER_BY_FIELDS.contains(orderBy)) {
                throw new IllegalArgumentException("Ungültiges orderBy-Feld: " + orderBy);
            }

            // Standardmäßig auf aufsteigende Sortierung setzen
            Sort.Direction sortDirection = Sort.Direction.ASC;
            // Wenn die Richtung "desc" ist, dann auf absteigende Sortierung setzen
            if ("desc".equalsIgnoreCase(direction)) {
                sortDirection = Sort.Direction.DESC;
            }

            // Überprüfen, ob das orderBy-Feld verschachtelt ist (z. B. "user.name")
            // Wenn es verschachtelt ist, dann muss die Sortierung über eine angepasste Specification gemacht werden
            if (OrderByUtils.FOREIGN_SET.contains(orderBy)) {
                // Sortierung über verschachtelte Felder wird NICHT im Pageable gesetzt,
                // sondern muss über eine angepasste Specification gemacht werden – siehe unten.
                spec = spec.and(
                        InventorySpecification.sortByNestedField(orderBy, Sort.Direction.fromString(direction)));

            // Wenn es nicht verschachtelt ist, dann kann die Sortierung direkt im Pageable gesetzt werden
            } else {
                Sort sort = Sort.by(sortDirection, orderBy);
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
            }
        }

        Page<Inventory> page = inventoriesRepository.findAll(spec, pageable);

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
        if (inventoriesRepository.existsById(dto.inventoriesId)) {
            // Wenn ja dann eine Exception werfen
            throw new IllegalArgumentException("Inventar-ID bereits vorhanden: " + dto.inventoriesId);
        }
        // ID setzen
        inventory.setId(dto.inventoriesId);

        // CostCenter holen oder erstellen
        CostCenter costCenter = costCentersRepository.findByName(dto.costCenter);
        if (costCenter == null) {
            costCenter = new CostCenter(dto.costCenter);
            costCentersRepository.save(costCenter);
        }
        inventory.setCostCenter(costCenter);

        // Company holen oder erstellen
        Company company = companiesRepository.findByName(dto.company)
                .orElseGet(() -> companiesRepository.save(new Company(dto.company)));
        inventory.setCompany(company);

        // User holen oder erstellen
        User user = resolveUser(dto.orderer);

        // Restliche Felder setzen
        inventory.setDescription(dto.description);
        inventory.setSerialNumber(dto.serialNumber);
        inventory.setPrice(dto.price);
        inventory.setLocation(dto.location);
        inventory.setUser(user);

        // saven um Tags auch das inventory hinzufügen zu können
        inventoriesRepository.save(inventory);

        // 6. Tags verknüpfen

        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            Set<Tag> tags = dto.getTags().stream()
                    .map(tagId -> tagsRepository.findById(tagId)
                            .orElseThrow(() -> new IllegalArgumentException("Tag not found: " + tagId)))
                    .collect(Collectors.toSet());
            inventory.setTags(tags);
        }

        inventoriesRepository.save(inventory);

        return inventoriesMapper.toDto(inventory);
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
        Optional<Inventory> inventory = inventoriesRepository.findById(id);
        if (inventory.isPresent()) {
            inventoriesRepository.delete(inventory.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
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
        Optional<Inventory> inventoryOptional = inventoriesRepository.findById(id);
        if (inventoryOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Inventory inventory = inventoryOptional.get();

        for (Map.Entry<String, Object> entry : patchData.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();

            switch (fieldName) {
                case "costCenter":
                    if (fieldValue != null) {
                        CostCenter costCenter = costCentersRepository.findByName(fieldValue.toString());
                        if (costCenter == null) {
                            costCenter = new CostCenter(fieldValue.toString());
                            costCentersRepository.save(costCenter);
                        }
                        inventory.setCostCenter(costCenter);
                    }
                    break;
                case "inventories_description":
                    if (fieldValue != null) {
                        inventory.setDescription((String) fieldValue);
                    }
                    break;
                case "company":
                    if (fieldValue != null) {
                        Company company = companiesRepository
                                .findByName(fieldValue.toString())
                                .orElseGet(() -> companiesRepository.save(new Company(fieldValue.toString())));
                        inventory.setCompany(company);
                    }
                    break;
                case "inventories_price":
                    if (fieldValue != null) {
                        inventory.setPrice(new BigDecimal(fieldValue.toString()));
                    }
                    break;
                case "inventories_serialNumber":
                    if (fieldValue != null) {
                        inventory.setSerialNumber((String) fieldValue);
                    }
                    break;
                case "inventories_location":
                    if (fieldValue != null) {
                        inventory.setLocation((String) fieldValue);
                    }
                    break;
                case "orderer":
                    if (fieldValue != null) {
                        Integer userId = null;
                        try {
                            userId = (Integer) fieldValue;
                        } catch (ClassCastException e) {
                            try {
                                userId = Integer.parseInt(fieldValue.toString());
                            } catch (NumberFormatException ex) {
                                break;
                            }
                        }
                        if (userId != null) {
                            User user = usersRepository.findById(userId).orElse(null);
                            if (user == null) {
                                user = new User();
                                user.setId(userId);
                                usersRepository.save(user);
                            }
                            inventory.setUser(user);
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        Inventory updatedInventory = inventoriesRepository.save(inventory);
        InventoriesResponseDTO responseDTO = inventoriesMapper.toDto(updatedInventory);
        return ResponseEntity.ok(responseDTO);
    }

    // Eventuell in UserService auslagern
    private User resolveUser(Object orderer) {
        if (orderer instanceof Integer userId) {
            return usersRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User-ID nicht gefunden: " + userId));
        } else if (orderer instanceof String userName) {
            return usersRepository.findByName(userName)
                    .orElseGet(() -> usersRepository.save(new User(userName)));
        }
        throw new IllegalArgumentException("orderer muss Integer oder String sein.");
    }
}
