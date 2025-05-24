package com.hs_esslingen.insy.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@Table(name = "tags")
public class Tags {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<InventoryTagRelations> inventoryRelations;

    @Builder
    public Tags() {
        this.inventoryRelations = new HashSet<>();
    }

    @Builder
    public Tags(String name) {
        this.name = name;
        inventoryRelations = new HashSet<>();
    }

    // Getter und Setter
    public void addInventoryRelation(InventoryTagRelations inventoryTagRelations) {
        this.inventoryRelations.add(inventoryTagRelations);
        inventoryTagRelations.setTag(this);
    }

    public void removeInventoryRelation(InventoryTagRelations inventoryTagRelations) {
        this.inventoryRelations.remove(inventoryTagRelations);
        inventoryTagRelations.setTag(null);
    }
}