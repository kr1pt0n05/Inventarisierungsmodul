
package com.hs_esslingen.insy.configuration;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hs_esslingen.insy.dto.InventoryCreateRequestDTO;
import com.hs_esslingen.insy.model.Company;
import com.hs_esslingen.insy.model.CostCenter;
import com.hs_esslingen.insy.model.Tag;
import com.hs_esslingen.insy.model.User;
import com.hs_esslingen.insy.repository.CompanyRepository;
import com.hs_esslingen.insy.repository.CostCenterRepository;
import com.hs_esslingen.insy.repository.TagRepository;
import com.hs_esslingen.insy.repository.UserRepository;
import com.hs_esslingen.insy.service.InventoryService;

import lombok.RequiredArgsConstructor;

/**
 * This class is used to seed the database with initial data.
 * It implements CommandLineRunner to execute code after the application context
 * is loaded.
 * Remove for production use.
 */

@Component
@RequiredArgsConstructor
@Profile("dev")
public class DatabaseSeeder implements CommandLineRunner {

        private final TagRepository tagRepository;
        private final CompanyRepository companyRepository;
        private final UserRepository userRepository;
        private final CostCenterRepository costCenterRepository;
        private final InventoryService inventoryService;

        @Override
        @Transactional
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

                // InventoryCreateRequestDTOs
                List<InventoryCreateRequestDTO> inventoryDTOs = List.of(
                                createDTO(2434, "Testgerät", "ABC123", "Lagerraum 1", new BigDecimal("999.99"),
                                                comp1.getName(), costCenter.getDescription(), testUser.getName(),
                                                List.of(tag1.getId())),
                                createDTO(2, "Dell Latitude 5530", "SN12345", "Büro 101", new BigDecimal("850.00"),
                                                comp2.getName(), costCenter.getDescription(), adminUser.getName(),
                                                List.of(tag1.getId())),
                                createDTO(3, "HP Z24 Monitor", "SN12346", "Büro 102", new BigDecimal("240.00"),
                                                comp1.getName(), costCenter2.getDescription(), user1.getName(),
                                                List.of(tag2.getId())),
                                createDTO(4, "Canon LaserJet 2300", "SN12347", "Druckerraum", new BigDecimal("410.50"),
                                                comp3.getName(), costCenter3.getDescription(), guestUser.getName(),
                                                List.of(tag3.getId())),
                                createDTO(5, "iPhone 14 Pro", "SN12348", "Büro 103", new BigDecimal("1240.00"),
                                                comp4.getName(), costCenter4.getDescription(), adminUser.getName(),
                                                List.of(tag4.getId())),
                                createDTO(6, "Samsung Galaxy S22", "SN12349", "F&E-Abteilung",
                                                new BigDecimal("1190.00"), comp4.getName(),
                                                costCenter4.getDescription(), testUser.getName(),
                                                List.of(tag4.getId())),
                                createDTO(7, "Logitech MX Master 3", "SN12350", "Schreibtisch 7",
                                                new BigDecimal("99.99"), comp2.getName(), costCenter3.getDescription(),
                                                user1.getName(), List.of()),
                                createDTO(8, "ThinkPad Dock", "SN12351", "Lager", new BigDecimal("189.00"),
                                                comp1.getName(), costCenter2.getDescription(), guestUser.getName(),
                                                List.of()),
                                createDTO(9, "MacBook Pro M2", "SN12352", "Büro 201", new BigDecimal("1899.00"),
                                                comp3.getName(), costCenter.getDescription(), testUser.getName(),
                                                List.of(tag1.getId())));

                for (InventoryCreateRequestDTO dto : inventoryDTOs) {
                        inventoryService.addInventory(dto);
                }
        }

        private InventoryCreateRequestDTO createDTO(Integer id, String description, String serial, String location,
                        BigDecimal price, String company, String costCenter,
                        String orderer, List<Integer> tags) {
                InventoryCreateRequestDTO dto = new InventoryCreateRequestDTO();
                dto.setInventoriesId(id);
                dto.setDescription(description);
                dto.setSerialNumber(serial);
                dto.setLocation(location);
                dto.setPrice(price);
                dto.setCompany(company);
                dto.setCostCenter(costCenter);
                dto.setOrderer(orderer);
                dto.setTags(tags);
                return dto;
        }
}
