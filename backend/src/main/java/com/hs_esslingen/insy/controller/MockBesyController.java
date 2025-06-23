package com.hs_esslingen.insy.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.hs_esslingen.insy.dto.ItemCreateDTO;
import com.hs_esslingen.insy.dto.OrderCreateDTO;

// Controller-Klasse, die den API-Call an BeSy simuliert
// ToDo: Remove for production
@RestController
@RequestMapping("/mock/besy")
public class MockBesyController {

        // Mock endpoint for testing purposes
        @PostMapping
        public void sendMockOrders() {
                List<ItemCreateDTO> items1 = List.of(
                                new ItemCreateDTO(1, "Laptop", new BigDecimal("999.99")),
                                new ItemCreateDTO(2, "Maus", new BigDecimal("29.99")));

                List<ItemCreateDTO> items2 = List.of(
                                new ItemCreateDTO(3, "Monitor", new BigDecimal("199.99")));

                List<OrderCreateDTO> orders = List.of(
                                new OrderCreateDTO(101, LocalDateTime.now().minusDays(2), "Tech GmbH", "IT-Abteilung",
                                                "Sandro Lappinski", new BigDecimal("1029.98"), items1),
                                new OrderCreateDTO(102, LocalDateTime.now().minusDays(1), "Office Supplies AG",
                                                "Marketing",
                                                "User 1", new BigDecimal("199.99"), items2));

                WebClient webClient = WebClient.builder()
                                .baseUrl("http://localhost:8080")
                                .defaultHeaders(headers -> headers.setBasicAuth("besy", "secret"))
                                .build();

                webClient.post()
                                .uri("/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(orders)
                                .retrieve()
                                .bodyToMono(Void.class)
                                .block();
        }

}
