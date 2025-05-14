package com.hs_esslingen.insy.controller;

import java.lang.foreign.Linker.Option;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.hs_esslingen.insy.model.Tags;
import com.hs_esslingen.insy.model.Users;
import com.hs_esslingen.insy.repository.CostCentersRepostitory;
import com.hs_esslingen.insy.repository.InventoriesRepository;
import com.hs_esslingen.insy.repository.TagsRepository;
import com.hs_esslingen.insy.repository.UsersRepository;
import com.hs_esslingen.insy.service.InventoriesService;

@RestController
@RequestMapping("/inventories")
public class InventoriesController {
    
    @Autowired
    private InventoriesRepository inventoriesRepository;
    private CostCentersRepostitory costCentersRepository;
    private UsersRepository usersRepository;
    private CostCentersRepostitory costCentersRepostitory;
    private TagsRepository tagsRepository;

    // Alle Elemente der Inventarisierungsliste abrufen
    @GetMapping
    public List<Inventories> getAllInventories() {
        return inventoriesRepository.getAllInventories();
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
            Inventories _inventories = inventoriesRepository.save(new Inventories (inventory.getCostCenters(), inventory.getUser(), inventory.getCompany(), inventory.getDescription(), inventory.getSerialNumber(), inventory.getPrice(), inventory.getLocation()));
            return new ResponseEntity<>(_inventories, HttpStatus.CREATED);
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
                            inventory.setCostCenter(costCenter);
                        }
                        inventory.setCostCenter((CostCenters) fieldValue);
                        break;
                    case "inventories_description":
                        if (fieldValue != null) {
                            inventory.setDescription((String) fieldValue);
                        }
                        break;
                    case "company":
                        if(field.getFieldValue() != null) {
                            // Existiert die Firma bereits in der Datenbank?
                            Companies company = companiesRepository.findByName(field.getFieldValue().toString());
                            // Wenn nicht, erstelle eine neue Firma
                            if (company == null) {
                                company = new Companies(field.getFieldValue().toString());
                                CompaniesRepository.save(company);
                            }
                            inventory.setCompany(company);
                        }
                        break;
                    
                    case "inventories_price":
                        if (fieldValue != null) {
                            inventory.setPrice((BigDecimal) fieldValue);
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
                                return ResponseEntity(status(HttpStatus.NOT_FOUND)
                                        .body("User with ID " + fieldValue + " not found"));
                            }
                        }
                        break;
                    case "tags":
                        // ToDo: Inventory-Tag-Relation setzen
                        if (fieldValue != null) {
                            List<Tags> tags = new ArrayList<>();
                            for (String tagName : (List<String>) fieldValue) {
                                Tags tag = tagsRepository.findByName(tagName);
                                if (tag == null) {
                                    tag = new Tags(tagName);
                                    tagsRepository.save(tag);
                                }
                                inventory.setTags(tag);
                            }
                            // ToDo: Wie mit mehreren Tags umgehen?
                        }
                        break;
                    default:
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
            Inventories updatedInventory = inventoriesService.updateInventory(inventory);
            return new ResponseEntity<>(updatedInventory, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
}
}
