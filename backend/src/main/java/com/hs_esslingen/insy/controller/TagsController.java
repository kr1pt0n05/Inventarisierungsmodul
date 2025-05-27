package com.hs_esslingen.insy.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hs_esslingen.insy.configuration.InventoryTagKey;
import com.hs_esslingen.insy.configuration.TagCreateRequest;
import com.hs_esslingen.insy.model.Inventories;
import com.hs_esslingen.insy.model.InventoryTagRelations;
import com.hs_esslingen.insy.model.Tags;
import com.hs_esslingen.insy.repository.InventoriesRepository;
import com.hs_esslingen.insy.repository.TagsRepository;

@RestController
public class TagsController {
    
    @Autowired
    private TagsRepository tagsRepository;
    
    @Autowired
    private InventoriesRepository inventoriesRepository;

    // Alle Tags abrufen
    @GetMapping("/tags")
    public ResponseEntity<Page<Tags>> getAllTags(@PageableDefault(size = 50) Pageable pageable) {
        try {
            Page<Tags> tags = tagsRepository.findAll(pageable);
            return ResponseEntity.ok(tags);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Einen Tag nach ID abrufen
    @GetMapping("/tags/{id}")
    public ResponseEntity<Tags> getTagById(@PathVariable Integer id) {
        try {
            Optional<Tags> tag = tagsRepository.findById(id);
            if (tag.isPresent()) {
                return ResponseEntity.ok(tag.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Neuen Tag erstellen
    @PostMapping("/tags")
    public ResponseEntity<Tags> createTag(@RequestBody TagCreateRequest request) {
        try {
            // Prüfen ob Tag mit diesem Namen bereits existiert
            if (tagsRepository.findByName(request.getName()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            Tags tag = new Tags(request.getName());
            Tags savedTag = tagsRepository.save(tag);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTag);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Tag löschen
    @DeleteMapping("/tags/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Integer id) {
        try {
            Optional<Tags> tag = tagsRepository.findById(id);
            if (!tag.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            tagsRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Tags eines Inventargegenstands abrufen
    @GetMapping("/inventories/{id}/tags")
    public ResponseEntity<List<Tags>> getTagsByInventoryId(@PathVariable Integer id) {
        try {
            Optional<Inventories> inventory = inventoriesRepository.findById(id);
            if (!inventory.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            List<Tags> tags = inventory.get().getTagRelations().stream()
                    .map(InventoryTagRelations::getTag)
                    .toList();
            
            return ResponseEntity.ok(tags);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Tag zu Inventargegenstand hinzufügen
    @PostMapping("/inventories/{id}/tags")
    public ResponseEntity<String> addTagToInventory(@PathVariable Integer id, 
                                                   @RequestBody AddTagRequest request) {
        try {
            System.out.println("Adding tags to inventory " + id + ": " + request);
            
            Optional<Inventories> inventoryOpt = inventoriesRepository.findById(id);
            
            if (!inventoryOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Inventory with ID " + id + " not found");
            }

            Inventories inventory = inventoryOpt.get();
            
            if (request.getTagIds() == null || request.getTagIds().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("No tag IDs provided");
            }

                        // Mehrere Tags gleichzeitig hinzufügen 
            for (Integer tagId : request.getTagIds()) {
                Optional<Tags> tagOpt = tagsRepository.findById(tagId);

                if (!tagOpt.isPresent()) {
                    return ResponseEntity.badRequest()
                            .body("Tag with ID " + tagId + " not found");
                }

                Tags tag = tagOpt.get();

                // Prüfen ob die Verknüpfung bereits existiert
                boolean relationExists = inventory.getTagRelations().stream()
                        .anyMatch(rel -> rel.getTag().getId().equals(tagId));

                if (!relationExists) {
                    // Statt neues Objekt zu erzeugen, prüfen ob es vielleicht schon geladen ist
                    InventoryTagKey key = new InventoryTagKey(id, tagId);

                    // Neue Verknüpfung erstellen
                    InventoryTagRelations relation = new InventoryTagRelations(inventory, tag);
                    relation.setId(key);

                    inventory.getTagRelations().add(relation);
                }
            }


            inventoriesRepository.save(inventory);
            return ResponseEntity.ok("Tags successfully added to inventory item");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding tags to inventory item: " + e.getMessage());
        }
    }

    // Tag von Inventargegenstand entfernen
    @DeleteMapping("/inventories/{id}/tags/{tagId}")
    public ResponseEntity<String> removeTagFromInventory(@PathVariable Integer id, 
                                                        @PathVariable("tagId") Integer tagId) {
        try {
            Optional<Inventories> inventoryOpt = inventoriesRepository.findById(id);
            
            if (!inventoryOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Inventory with ID " + id + " not found");
            }

            Inventories inventory = inventoryOpt.get();
            
            // Relation finden
            Optional<InventoryTagRelations> relationOpt = inventory.getTagRelations().stream()
                    .filter(rel -> rel.getTag().getId().equals(tagId))
                    .findFirst();

            if (!relationOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Tag with ID " + tagId + " is not assigned to inventory item " + id);
            }

            InventoryTagRelations relation = relationOpt.get();
            
            // Relation entfernen
            inventory.getTagRelations().remove(relation);
            relation.getTag().getInventoryRelations().remove(relation);

            inventoriesRepository.save(inventory);

            return ResponseEntity.ok("Tag successfully removed from inventory item");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error removing tag from inventory item");
        }
    }

    // Request DTO für Tag-zu-Inventar hinzufügen (mehrere Tags gleichzeitig)
    public static class AddTagRequest {
        private List<Integer> tagIds;

        // Standard Constructor
        public AddTagRequest() {}

        public List<Integer> getTagIds() {
            return tagIds;
        }

        public void setTagIds(List<Integer> tagIds) {
            this.tagIds = tagIds;
        }

        @Override
        public String toString() {
            return "AddTagRequest{tagIds=" + tagIds + "}";
        }
    }
}