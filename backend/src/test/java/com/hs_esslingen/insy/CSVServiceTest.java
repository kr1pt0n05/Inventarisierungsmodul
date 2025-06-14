package com.hs_esslingen.insy;

import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import com.hs_esslingen.insy.repository.CommentRepository;
import com.hs_esslingen.insy.repository.CompanyRepository;
import com.hs_esslingen.insy.repository.InventoryRepository;
import com.hs_esslingen.insy.repository.UserRepository;
import com.hs_esslingen.insy.service.CSVService;

class CSVServiceTest {

    @InjectMocks
    private CSVService csvService;

    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testImportCSVImproved_shouldImportDataCorrectly() throws Exception {
    // Gemockter CSV-Inhalt
    String csvContent = """
    Kst / Lab;InventarNr;Anzahl;Gerätetyp / Software;Firma;Preis / €;Datum;Seriennummer;Standort/Nutzer;Besteller;Kommentare
    IT42;12345;1;Laptop;ACME Inc.;1000;2024-05-01;SN123456;Room 101;John Doe;Kein Kommentar """;


        MockMultipartFile file = new MockMultipartFile(
                "file", "test.csv", "text/csv", csvContent.getBytes(StandardCharsets.UTF_8));

        // Keine User und Companies im Repository
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        when(companyRepository.findAll()).thenReturn(Collections.emptyList());

        csvService.importCSVImproved(file);

        // Eine Speicherung pro Repository
        verify(userRepository, times(1)).saveAll(anyCollection());
        verify(companyRepository, times(1)).saveAll(anyCollection());
        verify(inventoryRepository, times(1)).saveAll(anyCollection());
        verify(commentRepository, times(1)).saveAll(anyCollection());
    }
}
