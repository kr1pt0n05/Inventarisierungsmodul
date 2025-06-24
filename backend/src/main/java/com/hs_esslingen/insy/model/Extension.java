package com.hs_esslingen.insy.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "extensions")
public class Extension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "inventory_id", referencedColumnName = "id", nullable = false)
    private Inventory inventory;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    private String description;

    @Column(name = "serial_number", nullable = true)
    private String serialNumber;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "created_at", nullable = false)
    private final LocalDateTime createdAt = LocalDateTime.now(ZoneId.of("Europe/Berlin"));

    @Builder
    public Extension(Inventory inventory, Company company, String description, String serialNumber,
            BigDecimal price) {
        this.inventory = inventory;
        this.company = company;
        this.description = description;
        this.serialNumber = serialNumber;
        this.price = price;
    }
}
