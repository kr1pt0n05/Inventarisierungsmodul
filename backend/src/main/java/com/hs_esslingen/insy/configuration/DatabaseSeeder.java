
package com.hs_esslingen.insy.configuration;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.hs_esslingen.insy.model.Comments;
import com.hs_esslingen.insy.model.Companies;
import com.hs_esslingen.insy.model.Inventories;
import com.hs_esslingen.insy.model.InventoryTagRelations;
import com.hs_esslingen.insy.model.Tags;
import com.hs_esslingen.insy.repository.CompaniesRepository;
import com.hs_esslingen.insy.repository.CostCentersRepository;
import com.hs_esslingen.insy.repository.InventoriesRepository;
import com.hs_esslingen.insy.repository.TagsRepository;
import com.hs_esslingen.insy.repository.UsersRepository;
import com.hs_esslingen.insy.model.Users;
import com.hs_esslingen.insy.model.CostCenters;
import com.hs_esslingen.insy.repository.CommentsRepository;



/**
 * This class is used to seed the database with initial data.
 * It implements CommandLineRunner to execute code after the application context is loaded.
 * Remove for production use.
 */

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final TagsRepository tagsRepository;
    private final CompaniesRepository companiesRepository;
    private final InventoriesRepository inventoriesRepository;
    private final UsersRepository usersRepository;
    private final CostCentersRepository costCentersRepository;
    private final CommentsRepository commentsRepository;


    public DatabaseSeeder(
        TagsRepository tagsRepository,
        CompaniesRepository companiesRepository,
        InventoriesRepository inventoriesRepository,
        UsersRepository usersRepository,
        CostCentersRepository costCentersRepository,
        CommentsRepository commentsRepository
    ) {
        this.tagsRepository = tagsRepository;
        this.companiesRepository = companiesRepository;
        this.inventoriesRepository = inventoriesRepository;
        this.usersRepository = usersRepository;
        this.costCentersRepository = costCentersRepository;
        this.commentsRepository = commentsRepository;
    }

    @Override
    public void run(String... args) {
        // Tags
        Tags tag1 = new Tags("Laptop");
        Tags tag2 = new Tags("Monitor");

        tagsRepository.save(tag1);
        tagsRepository.save(tag2);

        // Companies
        Companies comp1 = new Companies("Gedankenfabrik GmbH");
        companiesRepository.save(comp1);

        // Users
        Users testUser = new Users();
        testUser.setName("Max Mustermann");
        usersRepository.save(testUser);

        // Companies
        CostCenters costCenter = new CostCenters();
        costCenter.setDescription("IT-Abteilung");
        costCentersRepository.save(costCenter);

        // Inventory
        Inventories inv = new Inventories();
        inv.setId(2434);
        inv.setDescription("Testgerät");
        inv.setPrice(new BigDecimal("999.99"));
        inv.setSerialNumber("ABC123");
        inv.setLocation("Lagerraum 1");
        inv.setCompany(comp1);  
        inv.setCostCenters(costCenter); 
        inv.setUser(testUser); 
        inv.addComment(new Comments(inv, testUser, "test"));
        inventoriesRepository.save(inv);


        
        InventoryTagRelations rel = new InventoryTagRelations();
        rel.setInventory(inv);
        rel.setTag(tag1);

        inv.getTagRelations().add(rel);
        tag1.getInventoryRelations().add(rel);

        inventoriesRepository.save(inv); // nochmal speichern, damit Relation mitkommt
    }
}


