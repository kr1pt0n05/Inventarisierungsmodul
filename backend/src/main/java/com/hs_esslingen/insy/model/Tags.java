package com.hs_esslingen.insy.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tags")
public class Tags {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "tags", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InventoryTagRelations> inventoryRelations;

    // Konstruktor
    public Tags() {
        this.inventoryRelations = new ArrayList<>();
    }
    public Tags(String name) {
        this.name = name;
        this.inventoryRelations = new ArrayList<>();
    }

    // Getter und Setter
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public List<InventoryTagRelations> getInventoryRelations() {
        return inventoryRelations;
    }
    public void setInventoryRelations(List<InventoryTagRelations> inventoryRelations) {
        this.inventoryRelations = inventoryRelations;
    }
    public void addInventoryRelation(InventoryTagRelations inventoryRelation) {
        this.inventoryRelations.add(inventoryRelation);
        inventoryRelation.setTag(this);
    }
    public void removeInventoryRelation(InventoryTagRelations inventoryRelation) {
        this.inventoryRelations.remove(inventoryRelation);
        inventoryRelation.setTag(null);
    }
}