
package com.hs_esslingen.insy.configuration;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.hs_esslingen.insy.model.Companies;
import com.hs_esslingen.insy.model.Inventories;
import com.hs_esslingen.insy.model.Tags;
import com.hs_esslingen.insy.repository.CompaniesRepository;
import com.hs_esslingen.insy.repository.CostCentersRepository;
import com.hs_esslingen.insy.repository.InventoriesRepository;
import com.hs_esslingen.insy.repository.TagsRepository;
import com.hs_esslingen.insy.repository.UsersRepository;
import com.hs_esslingen.insy.model.Users;
import com.hs_esslingen.insy.model.CostCenters;

/**
 * This class is used to seed the database with initial data.
 * It implements CommandLineRunner to execute code after the application context
 * is loaded.
 * Remove for production use.
 */

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final TagsRepository tagsRepository;
    private final CompaniesRepository companiesRepository;
    private final InventoriesRepository inventoriesRepository;
    private final UsersRepository usersRepository;
    private final CostCentersRepository costCentersRepository;

    public DatabaseSeeder(
            TagsRepository tagsRepository,
            CompaniesRepository companiesRepository,
            InventoriesRepository inventoriesRepository,
            UsersRepository usersRepository,
            CostCentersRepository costCentersRepository) {
        this.tagsRepository = tagsRepository;
        this.companiesRepository = companiesRepository;
        this.inventoriesRepository = inventoriesRepository;
        this.usersRepository = usersRepository;
        this.costCentersRepository = costCentersRepository;
    }

    @Override
    public void run(String... args) {
        // Tags
        Tags tag1 = new Tags("Laptop");
        Tags tag2 = new Tags("Monitor");
        Tags tag3 = new Tags("Drucker");
        Tags tag4 = new Tags("Smartphone");

        tagsRepository.saveAll(List.of(tag1, tag2, tag3, tag4));

        // Companies
        Companies comp1 = new Companies("Gedankenfabrik GmbH");
        Companies comp2 = new Companies("Tech Solutions AG");
        Companies comp3 = new Companies("Innovatech Ltd.");
        Companies comp4 = new Companies("Future Vision Inc.");
        companiesRepository.saveAll(List.of(comp1, comp2, comp3, comp4));

        // Users
        Users testUser = new Users("Max Mustermann");
        Users adminUser = new Users("Admin User");
        Users guestUser = new Users("Gast User");
        Users user1 = new Users("User 1");
        usersRepository.saveAll(List.of(testUser, adminUser, guestUser, user1));

        // Cost Centers
        CostCenters costCenter = new CostCenters("IT-Abteilung");
        CostCenters costCenter2 = new CostCenters("Marketing-Abteilung");
        CostCenters costCenter3 = new CostCenters("Vertrieb-Abteilung");
        CostCenters costCenter4 = new CostCenters("Forschung und Entwicklung");
        costCentersRepository.saveAll(List.of(costCenter, costCenter2, costCenter3, costCenter4));

        // Inventory 
        Inventories inv = new Inventories();
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
        inventoriesRepository.save(inv);

        // Weitere Inventories (IDs werden automatisch generiert – außer 1)
            List<Inventories> newInventories = List.of(
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

        inventoriesRepository.saveAll(newInventories);
    }

    // Hilfsmethode
    private Inventories createInventory(Integer id, String description, String serial, String location, BigDecimal price,
            Companies company, CostCenters costCenter, Users user, List<Tags> tags) {
        Inventories inventory = new Inventories();
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
        inventoriesRepository.save(inventory);
        return inventory;
    }
}
