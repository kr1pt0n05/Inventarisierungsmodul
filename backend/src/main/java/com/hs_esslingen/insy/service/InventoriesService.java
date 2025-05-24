package com.hs_esslingen.insy.service;

import com.hs_esslingen.insy.repository.CompaniesRepository;
import com.hs_esslingen.insy.repository.CostCentersRepository;
import com.hs_esslingen.insy.repository.InventoriesRepository;
import com.hs_esslingen.insy.repository.TagsRepository;
import com.hs_esslingen.insy.repository.UsersRepository;
import com.hs_esslingen.insy.configuration.InventorySpecification;
import com.hs_esslingen.insy.dto.InventoriesCreateRequestDTO;
import com.hs_esslingen.insy.dto.InventoriesResponseDTO;
import com.hs_esslingen.insy.mapper.InventoriesMapper;
import com.hs_esslingen.insy.model.CostCenters;
import com.hs_esslingen.insy.model.Inventories;
import com.hs_esslingen.insy.model.Tags;
import com.hs_esslingen.insy.model.Companies;
import com.hs_esslingen.insy.model.Users;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class InventoriesService {

    @Autowired
    private final InventoriesRepository inventoriesRepository;
    @Autowired
    private final UsersRepository usersRepository;
    @Autowired
    private final CompaniesRepository companiesRepository;
    @Autowired
    private final CostCentersRepository costCentersRepository;
    @Autowired
    private final TagsRepository tagsRepository;
    @Autowired
    private final InventoriesMapper inventoriesMapper;

    public InventoriesService(
            InventoriesRepository inventoriesRepository,
            UsersRepository usersRepository,
            CompaniesRepository companiesRepository,
            CostCentersRepository costCentersRepository,
            TagsRepository tagsRepository,
            InventoriesMapper inventoriesMapper) {
        this.inventoriesRepository = inventoriesRepository;
        this.usersRepository = usersRepository;
        this.companiesRepository = companiesRepository;
        this.costCentersRepository = costCentersRepository;
        this.tagsRepository = tagsRepository;
        this.inventoriesMapper = inventoriesMapper;
    }


    /**
     * Retrieves an inventory item by its ID.
     *
     * @param id the ID of the inventory item
     * @return ResponseEntity containing the inventory item if found,
     * or a 404 Not Found status if the item does not exist.
     */
    public ResponseEntity<InventoriesResponseDTO> getInventoryById(Integer id) {
        Optional<Inventories> inventory = inventoriesRepository.findById(id);
        if (inventory.isPresent()) {
            InventoriesResponseDTO responseDTO = inventoriesMapper.toDto(inventory.get());
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public Page<InventoriesResponseDTO> getAllInventories(
            List<Integer> tags,
            Integer minId,
            Integer maxId,
            Integer minPrice,
            Integer maxPrice,
            Boolean isDeinventoried,
            Pageable pageable) {

        Specification<Inventories> spec = Specification
                .where(InventorySpecification.hasTagId(tags))
                .and(InventorySpecification.idBetween(minId, maxId))
                .and(InventorySpecification.priceBetween(minPrice, maxPrice))
                .and(InventorySpecification.isDeinventoried(isDeinventoried));

        Page<Inventories> page = inventoriesRepository.findAll(spec, pageable);

        return page.map(inventoriesMapper::toDto);

    }

    /**
     * Adds a new inventory item.
     *
     * @param dto the DTO containing the inventory item data
     * @return ResponseEntity containing the created inventory item
     * @throws IllegalArgumentException if the inventory ID already exists
     */
    public InventoriesResponseDTO addInventory(InventoriesCreateRequestDTO dto) {
        Inventories inventory = new Inventories();

        // Existiert ein Inventar mit der Inventarnummer bereits?
        if (inventoriesRepository.existsById(dto.inventoriesId)) {
            // Wenn ja dann eine Exception werfen
            throw new IllegalArgumentException("Inventar-ID bereits vorhanden: " + dto.inventoriesId);
        }
        // 1. ID setzen
        inventory.setId(dto.inventoriesId);

        // 2. CostCenter holen oder erstellen
        CostCenters costCenter = costCentersRepository.findByName(dto.costCenter);
        if (costCenter == null) {
            costCenter = new CostCenters(dto.costCenter);
            costCentersRepository.save(costCenter);
        }
        inventory.setCostCenter(costCenter);

        // 3. Company holen oder erstellen
        Companies company = companiesRepository.findByName(dto.company)
                .orElseGet(() -> companiesRepository.save(new Companies(dto.company)));
        inventory.setCompany(company);

        // 4. User holen oder erstellen
        Users user = resolveUser(dto.orderer);

        // 5. Primitive Felder setzen
        inventory.setDescription(dto.description);
        inventory.setSerialNumber(dto.serialNumber);
        inventory.setPrice(dto.price);
        inventory.setLocation(dto.location);
        inventory.setUser(user);

        // saven um Tags auch das inventory hinzufügen zu können
        inventoriesRepository.save(inventory);

        // 6. Tags verknüpfen

        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            Set<Tags> tags = dto.getTags().stream()
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
     * @param id
     * @return ResponseEntity with no content if the item was deleted,
     * or a 404 Not Found status if the item does not exist.
     */
    public ResponseEntity<Void> deleteInventory(Integer id) {
        Optional<Inventories> inventory = inventoriesRepository.findById(id);
        if (inventory.isPresent()) {
            inventoriesRepository.delete(inventory.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Updates an existing inventory item with the provided patch data.
     * The patch data is a map where the keys are field names and the values are the new values for those fields.
     * @param id
     * @param patchData
     * @return ResponseEntity of type InventoriesResponseDTO containing the updated inventory item,
     * or a 404 Not Found status if the item does not exist.
     */
    public ResponseEntity<InventoriesResponseDTO> updateInventory(Integer id, Map<String, Object> patchData) {
        Optional<Inventories> inventoryOptional = inventoriesRepository.findById(id);
        if (inventoryOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Inventories inventory = inventoryOptional.get();

        for (Map.Entry<String, Object> entry : patchData.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();

            switch (fieldName) {
                case "costCenter":
                    if (fieldValue != null) {
                        CostCenters costCenter = costCentersRepository.findByName(fieldValue.toString());
                        if (costCenter == null) {
                            costCenter = new CostCenters(fieldValue.toString());
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
                        Companies company = companiesRepository
                                .findByName(fieldValue.toString())
                                .orElseGet(() -> companiesRepository.save(new Companies(fieldValue.toString())));
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
                            Users user = usersRepository.findById(userId).orElse(null);
                            if (user == null) {
                                user = new Users();
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

        Inventories updatedInventory = inventoriesRepository.save(inventory);
        InventoriesResponseDTO responseDTO = inventoriesMapper.toDto(updatedInventory);
        return ResponseEntity.ok(responseDTO);
    }

    // Eventuell in UserService auslagern
    private Users resolveUser(Object orderer) {
        if (orderer instanceof Integer userId) {
            return usersRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User-ID nicht gefunden: " + userId));
        } else if (orderer instanceof String userName) {
            return usersRepository.findByName(userName)
                    .orElseGet(() -> usersRepository.save(new Users(userName)));
        }
        throw new IllegalArgumentException("orderer muss Integer oder String sein.");
    }
}
