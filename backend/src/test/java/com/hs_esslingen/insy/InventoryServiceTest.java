package com.hs_esslingen.insy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hs_esslingen.insy.dto.InventoriesResponseDTO;
import com.hs_esslingen.insy.dto.InventoryCreateRequestDTO;
import com.hs_esslingen.insy.exception.BadRequestException;
import com.hs_esslingen.insy.mapper.InventoryMapper;
import com.hs_esslingen.insy.model.Company;
import com.hs_esslingen.insy.model.CostCenter;
import com.hs_esslingen.insy.model.Inventory;
import com.hs_esslingen.insy.model.Tag;
import com.hs_esslingen.insy.model.User;
import com.hs_esslingen.insy.repository.CompanyRepository;
import com.hs_esslingen.insy.repository.CostCenterRepository;
import com.hs_esslingen.insy.repository.InventoryRepository;
import com.hs_esslingen.insy.repository.TagRepository;
import com.hs_esslingen.insy.repository.UserRepository;
import com.hs_esslingen.insy.service.CompanyService;
import com.hs_esslingen.insy.service.CostCenterService;
import com.hs_esslingen.insy.service.InventoryService;
import com.hs_esslingen.insy.service.OrdererService;
import com.hs_esslingen.insy.service.TagService;

class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CostCenterRepository costCenterRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private InventoryMapper inventoryMapper;

    @Mock
    private CostCenterService costCenterService;

    @Mock
    private CompanyService companyService;

    @Mock
    private OrdererService ordererService;

    @Mock
    private TagService tagService;

    @InjectMocks
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getInventoryById_whenFound_returnsDto() {
        // Setup - Inventory mit ID 1
        Inventory inventory = new Inventory();
        inventory.setId(1);

        // Mock das Verhalten des Repositories
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inventory));

        // Mappe das Inventory zu einem DTO
        InventoriesResponseDTO dto = new InventoriesResponseDTO();
        when(inventoryMapper.toDto(inventory)).thenReturn(dto);

        // Methode aufrufen
        ResponseEntity<InventoriesResponseDTO> response = inventoryService.getInventoryById(1);

        // Überprüfung
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void getInventoryById_whenNotFound_returnsNotFound() {
        // Setup - Inventory mit ID 2 existiert nicht
        when(inventoryRepository.findById(2)).thenReturn(Optional.empty());

        ResponseEntity<InventoriesResponseDTO> response = inventoryService.getInventoryById(2);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void addInventory_whenIdExists_throwsException() {
        // Setup - Inventory mit ID 1 existiert bereits
        InventoryCreateRequestDTO dto = new InventoryCreateRequestDTO();
        dto.setInventoriesId(1);

        when(inventoryRepository.existsById(1)).thenReturn(true);

        // Überprüfung, Exception wird geworfen
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            inventoryService.addInventory(dto);
        });
    }

    @Test
    void addInventory_createsInventory() {
        // Setup - Neues Inventory mit ID 123
        InventoryCreateRequestDTO dto = new InventoryCreateRequestDTO();
        dto.setInventoriesId(123);
        dto.setCostCenter("IT");
        dto.setCompany("HS-Esslingen");
        dto.setOrderer("max.mustermann");
        dto.setDescription("Laptop");
        dto.setSerialNumber("ABC123");
        dto.setPrice(new BigDecimal("999.99"));
        dto.setLocation("Raum 101");
        dto.setTags(List.of(1)); 

        when(inventoryRepository.existsById(123)).thenReturn(false);
        
        // Mock des Verhalten der Service-Klassen
        when(costCenterService.resolveCostCenter("IT")).thenReturn(new CostCenter("IT"));
        when(companyService.resolveCompany("HS-Esslingen")).thenReturn(new Company("HS-Esslingen"));
        when(ordererService.resolveUser("max.mustermann")).thenReturn(new User("max.mustermann"));
        when(tagRepository.findById(1)).thenReturn(Optional.of(new Tag(1, "IT", Collections.emptySet())));

        // Mock für das erneute Finden des Inventories nach dem Speichern
        Inventory savedInventory = new Inventory();
        savedInventory.setId(123);
        when(inventoryRepository.findById(123)).thenReturn(Optional.of(savedInventory));

        InventoriesResponseDTO responseDTO = new InventoriesResponseDTO();
        when(inventoryMapper.toDto(any())).thenReturn(responseDTO);

        InventoriesResponseDTO result = inventoryService.addInventory(dto);

        // Überprüfung
        assertEquals(responseDTO, result);
        verify(inventoryRepository, times(1)).save(any(Inventory.class)); // Nur 1x save() erwartet
        verify(tagService, times(1)).addTagsToInventory(eq(123), eq(List.of(1)));
    }
}