/*

package com.hs_esslingen.insy.configuration;

import java.math.BigDecimal;


import java.util.List;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.hs_esslingen.insy.model.Comment;
import com.hs_esslingen.insy.model.Company;
import com.hs_esslingen.insy.model.Inventory;
import com.hs_esslingen.insy.model.Tag;
import com.hs_esslingen.insy.repository.CompanyRepository;
import com.hs_esslingen.insy.repository.CostCenterRepository;
import com.hs_esslingen.insy.repository.InventoryRepository;
import com.hs_esslingen.insy.repository.TagRepository;
import com.hs_esslingen.insy.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import com.hs_esslingen.insy.model.User;
import com.hs_esslingen.insy.model.CostCenter;

*/
/**
 * This class is used to seed the database with initial data.
 * It implements CommandLineRunner to execute code after the application context
 * is loaded.
 * Remove for production use.
 *//*


@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final TagRepository tagRepository;
    private final CompanyRepository companyRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final CostCenterRepository costCenterRepository;


    @Override
    public void run(String... args) {
        // Tags
        Tag tag1 = new Tag("Laptop");
        Tag tag2 = new Tag("Monitor");
        Tag tag3 = new Tag("Drucker");
        Tag tag4 = new Tag("Smartphone");

        tagRepository.saveAll(List.of(tag1, tag2, tag3, tag4));

        // Companies
        Company comp1 = new Company("Gedankenfabrik GmbH");
        Company comp2 = new Company("Tech Solutions AG");
        Company comp3 = new Company("Innovatech Ltd.");
        Company comp4 = new Company("Future Vision Inc.");
        companyRepository.saveAll(List.of(comp1, comp2, comp3, comp4));

        // Users
        User testUser = new User("Max Mustermann");
        User adminUser = new User("Admin User");
        User guestUser = new User("Gast User");
        User user1 = new User("User 1");
        userRepository.saveAll(List.of(testUser, adminUser, guestUser, user1));

        // Cost Centers
        CostCenter costCenter = new CostCenter("IT-Abteilung");
        CostCenter costCenter2 = new CostCenter("Marketing-Abteilung");
        CostCenter costCenter3 = new CostCenter("Vertrieb-Abteilung");
        CostCenter costCenter4 = new CostCenter("Forschung und Entwicklung");
        costCenterRepository.saveAll(List.of(costCenter, costCenter2, costCenter3, costCenter4));

        // Inventory 
        Inventory inv = new Inventory();
        inv.setId(2434);
        inv.setDescription("Testgerät");
        inv.setPrice(new BigDecimal("999.99"));
        inv.setSerialNumber("ABC123");
        inv.setLocation("Lagerraum 1");
        inv.setCompany(comp1);
        inv.setCostCenter(costCenter);
        inv.setUser(testUser);
        inv.getTags().add(tag1);
        tag1.getInventories().add(inv);
        inv.addComment(new Comment(inv, testUser, "test"));
        inventoryRepository.save(inv);

        // Weitere Inventories (IDs werden automatisch generiert – außer 1)
            List<Inventory> newInventories = List.of(
        createInventory(2, "Dell Latitude 5530", "SN12345", "Büro 101", new BigDecimal("850.00"), comp2, costCenter, adminUser, List.of(tag1)),
        createInventory(3, "HP Z24 Monitor", "SN12346", "Büro 102", new BigDecimal("240.00"), comp1, costCenter2, user1, List.of(tag2)),
        createInventory(4, "Canon LaserJet 2300", "SN12347", "Druckerraum", new BigDecimal("410.50"), comp3, costCenter3, guestUser, List.of(tag3)),
        createInventory(5, "iPhone 14 Pro", "SN12348", "Büro 103", new BigDecimal("1240.00"), comp4, costCenter4, adminUser, List.of(tag4)),
        createInventory(6, "Samsung Galaxy S22", "SN12349", "F&E-Abteilung", new BigDecimal("1190.00"), comp4, costCenter4, testUser, List.of(tag4)),
        createInventory(7, "Logitech MX Master 3", "SN12350", "Schreibtisch 7", new BigDecimal("99.99"), comp2, costCenter3, user1, List.of()),
        createInventory(8, "ThinkPad Dock", "SN12351", "Lager", new BigDecimal("189.00"), comp1, costCenter2, guestUser, List.of()),
        createInventory(9, "MacBook Pro M2", "SN12352", "Büro 201", new BigDecimal("1899.00"), comp3, costCenter, testUser, List.of(tag1))
    );

        // Setze bei zwei Inventories isDeinventoried = true
        newInventories.get(2).setIsDeinventoried(true); // Canon Drucker
        newInventories.get(7).setIsDeinventoried(true); // MacBook
        
        inventoryRepository.saveAll(newInventories);
    }

    // Hilfsmethode
    private Inventory createInventory(Integer id, String description, String serial, String location, BigDecimal price,
            Company company, CostCenter costCenter, User user, List<Tag> tags) {
        Inventory inventory = new Inventory();
        inventory.setId(id);
        inventory.setDescription(description);
        inventory.setSerialNumber(serial);
        inventory.setLocation(location);
        inventory.setPrice(price);
        inventory.setCompany(company);
        inventory.setCostCenter(costCenter);
        inventory.setUser(user);
        tags.forEach(tag -> {
            inventory.getTags().add(tag);
            tag.getInventories().add(inventory);
        });
        inventoryRepository.save(inventory);
        return inventory;
    }
}
*/
