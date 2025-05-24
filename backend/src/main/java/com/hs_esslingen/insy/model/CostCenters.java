package com.hs_esslingen.insy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@Builder
@NoArgsConstructor
@Table(name = "cost_centers")
public class CostCenters {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    String description;

    @Column(name = "is_archived")
    @Builder.Default
    private Boolean isArchived = false;

    @OneToMany(mappedBy = "costCenters")
    @Builder.Default
    @JsonManagedReference
    private List<Inventories> inventories = new ArrayList<>();

    // Konstruktor

    @Builder
    public CostCenters(String description) {
        this.description = description;
        this.isArchived = false;
        this.inventories = new ArrayList<>();
    }

    @Builder
    public CostCenters(String description, Boolean isArchived) {
        this.description = description;
        this.isArchived = isArchived;
        this.inventories = new ArrayList<>();
    }

    // Getter und Setter
    public void addInventory(Inventories inventory) {
        this.inventories.add(inventory);
        inventory.setCostCenters(this);
    }

    public void removeInventory(Inventories inventory) {
        this.inventories.remove(inventory);
        inventory.setCostCenters(null);
    }
}