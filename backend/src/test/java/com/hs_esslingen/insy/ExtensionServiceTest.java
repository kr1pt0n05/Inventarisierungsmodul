package com.hs_esslingen.insy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.hs_esslingen.insy.dto.ExtensionCreateDTO;
import com.hs_esslingen.insy.dto.ExtensionResponseDTO;
import com.hs_esslingen.insy.mapper.ExtensionMapper;
import com.hs_esslingen.insy.model.Company;
import com.hs_esslingen.insy.model.Extension;
import com.hs_esslingen.insy.model.Inventory;
import com.hs_esslingen.insy.repository.CompanyRepository;
import com.hs_esslingen.insy.repository.ExtensionRepository;
import com.hs_esslingen.insy.repository.InventoryRepository;
import com.hs_esslingen.insy.service.ExtensionService;
import com.hs_esslingen.insy.service.InventoryService;

class ExtensionServiceTest {

    @InjectMocks
    private ExtensionService extensionService;

    @Mock
    private ExtensionRepository extensionRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private ExtensionMapper extensionMapper;

    @Mock
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllExtensions_Success() {

        //Setup - Inventar mit einer Extension
        Inventory inventory = new Inventory();
        inventory.setId(1);
        Extension ext = new Extension();
        ext.setId(10);
        inventory.setExtensions(new ArrayList<>(List.of(ext)));

        ExtensionResponseDTO dto = new ExtensionResponseDTO();

        //Simuliert den Datenbankzugruiff
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inventory));
        when(extensionMapper.toDto(ext)).thenReturn(dto);

        //Überprüfen, ob ein dto zurückgegeben wird
        List<ExtensionResponseDTO> result = extensionService.getAllExtensions(1);
        assertEquals(1, result.size());
        verify(inventoryRepository).findById(1);
        verify(extensionMapper).toDto(ext);
    }

    @Test
    void testGetAllExtensions_InventoryNotFound() {
        when(inventoryRepository.findById(2)).thenReturn(Optional.empty());
        // Erwartet eine Exception
        assertThrows(RuntimeException.class, () -> extensionService.getAllExtensions(2));
    }

    @Test
    void testAddExtension_NewCompany() {

        // Setup - Inventar ohne Extensions
        Inventory inv = new Inventory();
        inv.setId(1);
        inv.setExtensions(new ArrayList<>());

        // DTO mit Extension-Daten
        ExtensionCreateDTO dto = new ExtensionCreateDTO();
        dto.setCompanyName("Acme");
        dto.setSerialNumber("SN1");

        // Neue Extension und Company
        Extension entity = new Extension();
        Company savedCompany = new Company();
        savedCompany.setId(5);
        savedCompany.setName("Acme");

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inv));
        when(extensionMapper.toEntity(dto)).thenReturn(entity);
        when(companyRepository.findByName("Acme")).thenReturn(Optional.empty());
        when(companyRepository.save(any(Company.class))).thenReturn(savedCompany);
        when(extensionMapper.toDto(entity)).thenReturn(new ExtensionResponseDTO());
        
        // Aufruf der Methode
        ExtensionResponseDTO result = extensionService.addExtension(1, dto);

        // Überprüfung
        assertNotNull(result);
        assertEquals(savedCompany, entity.getCompany());
        assertTrue(inv.getExtensions().contains(entity));
    }

    @Test
    void testGetExtensionById_Success() {

        // Setup - Inventar mit einer Extension
        Inventory inv = new Inventory();
        inv.setId(1);
        Extension ext = new Extension();
        ext.setId(7);
        inv.setExtensions(new ArrayList<>(List.of(ext)));

        ExtensionResponseDTO dto = new ExtensionResponseDTO();
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inv));
        when(extensionMapper.toDto(ext)).thenReturn(dto);

        ExtensionResponseDTO result = extensionService.getExtensionById(1, 7);

        assertEquals(dto, result);
    }

    @Test
    void testGetExtensionById_NotFound() {
        Inventory inv = new Inventory();
        inv.setId(1);
        inv.setExtensions(new ArrayList<>());

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inv));
        assertThrows(RuntimeException.class, () -> extensionService.getExtensionById(1, 99));
    }

    @Test
    void testUpdateExtension_ChangeCompanyAndPrice() {

        // Setup - Bestehende Extension und Inventar
        Extension ext = new Extension();
        ext.setId(8);
        ext.setPrice(new BigDecimal("50.00"));
        Inventory inv = new Inventory();
        inv.setPrice(new BigDecimal("200.00"));
        ext.setInventory(inv);
        Company old = new Company();
        old.setName("Old");
        ext.setCompany(old);

        // DTO mit neuen Daten
        ExtensionCreateDTO dto = new ExtensionCreateDTO();
        dto.setCompanyName("NewCo");
        dto.setPrice(new BigDecimal("80.00"));
        dto.setDescription("Desc");

        Company newComp = new Company();
        newComp.setName("NewCo");

        when(extensionRepository.findById(8)).thenReturn(Optional.of(ext));
        when(companyRepository.findByName("NewCo")).thenReturn(Optional.empty());
        when(companyRepository.save(any(Company.class))).thenReturn(newComp);
        when(extensionRepository.save(ext)).thenReturn(ext);
        when(extensionMapper.toDto(ext)).thenReturn(new ExtensionResponseDTO());

        ExtensionResponseDTO result = extensionService.updateExtension(1, 8, dto);

        // Überprüfung
        assertNotNull(result);
        assertEquals(new BigDecimal("230.00"), inv.getPrice());
        assertEquals(newComp, ext.getCompany());
    }

    @Test
    void testDeleteExtension_Success() {

        //Setup - Inventar mit einer Extension
        Inventory inv = new Inventory();
        inv.setId(1);
        Extension ext = new Extension();
        ext.setId(15);
        inv.setExtensions(new ArrayList<>(List.of(ext)));

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inv));
        when(extensionRepository.findById(15)).thenReturn(Optional.of(ext));

        extensionService.deleteExtension(1, 15);

        verify(extensionRepository).delete(ext);
        assertFalse(inv.getExtensions().contains(ext));
    }

    @Test
    void testDeleteExtension_NotFoundInventory() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> extensionService.deleteExtension(1, 5));
    }

    @Test
    void testDeleteExtension_NotFoundExtension() {
        Inventory inv = new Inventory();
        inv.setId(1);
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inv));
        when(extensionRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> extensionService.deleteExtension(1, 99));
    }
}