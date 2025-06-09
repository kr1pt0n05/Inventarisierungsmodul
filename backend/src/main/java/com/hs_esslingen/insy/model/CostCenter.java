package com.hs_esslingen.insy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cost_centers")
public class CostCenter {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    String description; // ToDo: Feld überhaupt nötig? Kostenstelle ist nur als Nummer in Inventarliste vorhanden, diese könnte man als Primary Key verwenden

    @Column(name = "is_archived")
    @Builder.Default
    private Boolean isArchived = false;

    @OneToMany(mappedBy = "costCenter")
    @Builder.Default
    @JsonManagedReference
    private List<Inventory> inventories = new ArrayList<>();

    // Konstruktor

    @Builder
    public CostCenter(String description) {
        this.description = description;
        this.isArchived = false;
        this.inventories = new ArrayList<>();
    }

    @Builder
    public CostCenter(String description, Boolean isArchived) {
        this.description = description;
        this.isArchived = isArchived;
        this.inventories = new ArrayList<>();
    }

    // Getter und Setter
    public void addInventory(Inventory inventory) {
        this.inventories.add(inventory);
        inventory.setCostCenter(this);
    }

    public void removeInventory(Inventory inventory) {
        this.inventories.remove(inventory);
        inventory.setCostCenter(null);
    }
}