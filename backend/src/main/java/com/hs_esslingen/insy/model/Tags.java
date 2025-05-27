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

@Entity
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
    private Set<InventoryTagRelations> inventoryRelations = new HashSet<>();

    // Konstruktor
    public Tags() {

    }
    public Tags(String name) {
        this.name = name;
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
    public Set<InventoryTagRelations> getInventoryRelations() {
        return inventoryRelations;
    }
    public void setInventoryRelations(Set<InventoryTagRelations> inventoryRelations) {
        this.inventoryRelations = inventoryRelations;
    }
    public void addInventoryRelation(InventoryTagRelations inventoryTagRelations) {
        this.inventoryRelations.add(inventoryTagRelations);
        inventoryTagRelations.setTag(this);
    }
    public void removeInventoryRelation(InventoryTagRelations inventoryTagRelations) {
        this.inventoryRelations.remove(inventoryTagRelations);
        inventoryTagRelations.setTag(null);
    }
}
