package com.hs_esslingen.insy.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hs_esslingen.insy.dto.ExtensionCreateDTO;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ExtensionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddExtension_ReturnsId() throws Exception {
        ExtensionCreateDTO dto = new ExtensionCreateDTO();
        dto.setDescription("Testerweiterung");
        dto.setCompanyName("Firma 1");
        dto.setPrice(new BigDecimal("100.00"));
        dto.setSerialNumber("SNTEST999");

        String jsonBody = objectMapper.writeValueAsString(dto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/inventories/2/components")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        JsonNode json = objectMapper.readTree(responseBody);

        assertNotNull(json.get("id"), "Die ID der Extension sollte nicht null sein");
        assertTrue(json.get("id").asInt() > 0, "Die ID sollte größer als 0 sein");
    }
}
