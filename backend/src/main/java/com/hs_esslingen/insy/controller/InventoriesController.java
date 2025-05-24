package com.hs_esslingen.insy.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.hs_esslingen.insy.configuration.InventoryCreateRequest;
import com.hs_esslingen.insy.configuration.InventorySpecification;
import com.hs_esslingen.insy.model.Companies;
import com.hs_esslingen.insy.model.CostCenters;
import com.hs_esslingen.insy.model.Inventories;
import com.hs_esslingen.insy.model.InventoryTagRelations;
import com.hs_esslingen.insy.model.Tags;
import com.hs_esslingen.insy.model.Users;
import com.hs_esslingen.insy.repository.CompaniesRepository;
import com.hs_esslingen.insy.repository.CostCentersRepository;
import com.hs_esslingen.insy.repository.InventoriesRepository;
import com.hs_esslingen.insy.repository.TagsRepository;
import com.hs_esslingen.insy.repository.UsersRepository;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("/inventories")
public class InventoriesController {
    
    @Autowired
    private InventoriesRepository inventoriesRepository;
    @Autowired
    private CostCentersRepository costCentersRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CompaniesRepository companiesRepository;
    @Autowired
    private TagsRepository tagsRepository;

    // Alle Elemente der Inventarisierungsliste abrufen
    @GetMapping
    public Page<Inventories> getAllInventories(
        @RequestParam(required = false) List<Integer> tags,
        @RequestParam(required = false) Integer minId,
        @RequestParam(required = false) Integer maxId,
        @RequestParam(required = false) Integer minPrice,
        @RequestParam(required = false) Integer maxPrice,
        @RequestParam(required = false) Boolean isDeinventoried,
        @PageableDefault(size = 50) Pageable pageable) {

        Specification<Inventories> spec = Specification
        .where(InventorySpecification.hasTagId(tags))
        .and(InventorySpecification.idBetween(minId, maxId))
        .and(InventorySpecification.isDeinventoried(isDeinventoried));
        return inventoriesRepository.findAll(spec, pageable);
    }

    // Ein Element der Inventarisierungsliste abrufen
    @GetMapping("/{id}")
    public ResponseEntity<Inventories> getInventoryById(@PathVariable ("id") Integer id) {

        Optional<Inventories> inventory = inventoriesRepository.findById(id);
        if (inventory.isPresent()) {
            return new ResponseEntity<>(inventory.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Ein Element der Inventarisierungsliste hinzufügen
    @PostMapping
    public ResponseEntity<Inventories> addInventory(@RequestBody InventoryCreateRequest request) {

        // Inventargegenstand mit ID schon vorhanden?
        if(inventoriesRepository.existsById(request.inventories_id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // ID bereits vorhanden
        }

        try {
            // CostCenter holen oder erstellen
            CostCenters costCenter = costCentersRepository.findByName(request.cost_center);
            if (costCenter == null) {
                costCenter = new CostCenters(request.cost_center);
                costCentersRepository.save(costCenter);
            }

            // Company holen oder erstellen
            Companies company = companiesRepository.findByName(request.company)
                .orElseGet(() -> companiesRepository.save(new Companies(request.company)));

            // Existiert der User in der Datenbank?
            Users user = null;
            // Wenn user als integer-id übergeben
            if (request.orderer instanceof Integer userId) {
                user = usersRepository.findById(userId).orElse(null);
                if (user == null) {
                    return ResponseEntity.badRequest().body(null); // ID ungültig
                }
            // Wenn user als string übergeben
            } else if (request.orderer instanceof String userName) {
                user = usersRepository.findByName(userName)
                    .orElseGet(() -> usersRepository.save(new Users(userName)));
            } else {
                return ResponseEntity.badRequest().build(); // falscher Typ
            }

            Inventories inventory = new Inventories(
                request.inventories_id,
                costCenter,
                user,
                company,
                request.inventories_description,
                request.inventories_serial_number,
                request.inventories_price,
                request.inventories_location
            );

            // Wenn Tags übergeben wurden: Tags mit inventar verknüpfen
            if (request.tags != null) {
                for (Integer tagId : request.tags) {
                    // Ist die Tag-Id gültig?
                    Tags tag = tagsRepository.findById(tagId).orElse(null);
                    if (tag != null) {
                        // Verknüpfung zwischen Inventar und Tag erstellen
                        // und in die Listen der beiden Objekte einfügen
                        InventoryTagRelations rel = new InventoryTagRelations();
                        rel.setInventory(inventory);
                        rel.setTag(tag);

                        inventory.getTagRelations().add(rel);
                        tag.getInventoryRelations().add(rel);
                    }
                }
            }

            inventoriesRepository.save(inventory);
            usersRepository.save(user);
            user.addInventory(inventory);
            company.addInventory(inventory);
            return new ResponseEntity<>(inventory, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace(); // zum debuggen
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Ein Element der Inventarisierungsliste löschen
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Integer id) {
        Optional<Inventories> inventory = inventoriesRepository.findById(id);
        if (!inventory.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            inventoriesRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Ein Element der Inventarisierungsliste aktualisieren
    // Aus dem Request-Body werden alle Felder ausgelesen, die nicht null sind
    // und dann in PatchFieldDTO fields gespeichert
    // und an die Inventarisierungsservice-Methode weitergegeben
    @PatchMapping("/{id}")
    public ResponseEntity<Inventories> updateInventory(@PathVariable Integer id,
            @RequestBody Map<String, Object> patchData) {

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
                        // Existiert die Kostenstelle bereits in der Datenbank?
                        CostCenters costCenter = costCentersRepository.findByName(fieldValue.toString());
                        // Wenn nicht, erstelle eine neue Kostenstelle
                        if (costCenter == null) {
                            costCenter = new CostCenters(fieldValue.toString());
                            costCentersRepository.save(costCenter);
                        }
                        inventory.setCostCenters(costCenter);
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
                                // Existiert die Firma bereits in der Datenbank?
                                .findByName(fieldValue.toString())
                                // Wenn nicht, erstelle eine neue Firma
                                .orElseGet(() -> {
                                    Companies newCompany = new Companies(fieldValue.toString());
                                    return companiesRepository.save(newCompany);
                                });
                        // Setze die Firma in der Inventarisierung
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
                    // Annahme: user-id wird übergeben
                    // und nicht der username
                    if (fieldValue != null) {
                        Users user = usersRepository.findById((Integer) fieldValue).orElse(null);
                        // Existiert der User bereits in der Datenbank?
                        if (user != null) {
                            inventory.setUser(user);
                        } else {

                            // Wenn nicht, erstelle einen neuen User
                            user = new Users();
                            user.setId((Integer) fieldValue);
                            usersRepository.save(user);
                            inventory.setUser(user);
                        }
                    }
                    break;
                default:
                    // Unbekanntes Feld, ignoriere es
                    break;
            }
        }
        Inventories updatedInventory = inventoriesRepository.save(inventory);
        return new ResponseEntity<>(updatedInventory, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> putMethodName(@PathVariable Integer id, @RequestBody Inventories entity) {
        //TODO: process PUT request
        Optional<Inventories> inventory = inventoriesRepository.findById(id);

        if(inventory.isPresent()) {
            Inventories existingInventory = inventory.get();
            // Update the existing inventory with the new entity data
            existingInventory.setDescription(entity.getDescription());
            existingInventory.setSerialNumber(entity.getSerialNumber());
            existingInventory.setPrice(entity.getPrice());
            existingInventory.setLocation(entity.getLocation());
            inventoriesRepository.save(existingInventory);

            return ResponseEntity.ok("Inventory with ID " + id + " updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Inventory with ID " + id + " not found");
        }
         // Return the updated entity
        
    }
}
