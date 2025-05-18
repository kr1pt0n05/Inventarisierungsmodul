package com.hs_esslingen.insy.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import com.hs_esslingen.insy.configuration.PatchFieldDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<Inventories> getAllInventories() {
        return inventoriesRepository.findAll();
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
    public ResponseEntity<Inventories> addInventory(@RequestBody Inventories inventory) {
        try {
            Inventories inventories = inventoriesRepository.save(new Inventories (inventory.getId(), inventory.getCostCenters(), inventory.getUser(), inventory.getCompany(), inventory.getDescription(), inventory.getSerialNumber(), inventory.getPrice(), inventory.getLocation()));
            return new ResponseEntity<>(inventories, HttpStatus.CREATED);
        } catch (Exception e) {
            // TODO: handle exception
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
            // TODO: handle exception
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Ein Element der Inventarisierungsliste aktualisieren
    // Aus dem Request-Body werden alle Felder ausgelesen, die nicht null sind
    // und dann in PatchFieldDTO fields gespeichert
    // und an die Inventarisierungsservice-Methode weitergegeben
    @PatchMapping("/{id}")
    public ResponseEntity<Inventories> updateInventory(@PathVariable Integer id, @RequestBody List<PatchFieldDTO> fields) {
        Optional<Inventories> inventoryoptional = inventoriesRepository.findById(id);
        if (inventoryoptional.isPresent()) {
            Inventories inventory = inventoryoptional.get();
            for (PatchFieldDTO field : fields) {
                String fieldName = field.getFieldName();
                Object fieldValue = field.getFieldValue();
                if ("null".equals(fieldValue)) {
                    fieldValue = null;
                }
                    
                switch (fieldName) {

                    case "costCenter":
                        if(field.getFieldValue() != null) {
                            // Existiert die Kostenstelle bereits in der Datenbank?
                            CostCenters costCenter = costCentersRepository.findByDescriptionContainingCostCenters(field.getFieldValue().toString());
                            // Wenn nicht, erstelle eine neue Kostenstelle
                            if (costCenter == null) {
                                costCenter = new CostCenters(field.getFieldValue().toString());
                                costCentersRepository.save(costCenter);
                            }
                            inventory.setCostCenters(costCenter);
                        }
                        inventory.setCostCenters((CostCenters) fieldValue);
                        break;
                    case "inventories_description":
                        if (fieldValue != null) {
                            inventory.setDescription((String) fieldValue);
                        }
                        break;
                    case "company":
                        if(field.getFieldValue() != null) {
                            
                            Companies company = companiesRepository
                                // Existiert die Firma bereits in der Datenbank?
                                .findByName(field.getFieldValue().toString())
                                // Wenn nicht, erstelle eine neue Firma
                                .orElseGet(() -> {
                                    Companies newCompany = new Companies(field.getFieldValue().toString());
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
                        if (field.getFieldValue() != null) {
                            Users user = usersRepository.findById((Integer) fieldValue).orElse(null);
                            // Existiert der User bereits in der Datenbank?
                            if (user != null) {
                                inventory.setUser(user);
                            } else {
                                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                            }
                        }
                        break;
                    case "tags":
                        if (fieldValue != null){
                            List<String> tagInputs = (List<String>) fieldValue;
                            for (String tagInput : tagInputs) {
                                Tags tag = null;

                                try {
                                    Integer tagId = Integer.parseInt(tagInput);
                                    tag = tagsRepository.findById(tagId).orElse(null);
                                } catch (NumberFormatException e) {
                                    // kein Integer, also Name
                                }

                                if (tag == null) {
                                    tag = tagsRepository.findByName(tagInput).orElse(null);
                                    if (tag == null) {
                                        tag = new Tags(tagInput);
                                        tagsRepository.save(tag);
                                    }
                                }

                                // Überprüfen, ob die Beziehung bereits existiert
                                Tags finalTag = tag;

                                boolean alreadyLinked = inventory.getTagRelations().stream()
                                    .anyMatch(rel -> rel.getTag().equals(finalTag));

                                if (!alreadyLinked) {
                                    InventoryTagRelations rel = new InventoryTagRelations();
                                    rel.setInventory(inventory);
                                    rel.setTag(finalTag);

                                    inventory.getTagRelations().add(rel);
                                    finalTag.getInventoryRelations().add(rel);
                                }
                            }
                        }
                        break;

                    default:
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
            Inventories updatedInventory = inventoriesRepository.save(inventory);
            return new ResponseEntity<>(updatedInventory, HttpStatus.OK);
        } else {
            return  new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
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
