package com.hs_esslingen.insy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;



@Entity
@Table(name = "cost_centers")
public class CostCenters {
    
    @Id
    @Column(nullable = false)
    private String id;

    String description;

    @Column(name = "is_archived")
    private Boolean isArchived;

    @OneToMany(mappedBy = "costCenters")
    @JsonManagedReference
    private List<Inventories> inventories;

    // Konstruktor

    public CostCenters() {
        this.inventories = new ArrayList<>();
        this.isArchived = false;
    }

    public CostCenters(String description) {
        this.description = description;
        this.isArchived = false;
        this.inventories = new ArrayList<>();
    }
    
    public CostCenters(String description, Boolean isArchived) {
        this.description = description;
        this.isArchived = isArchived;
        this.inventories = new ArrayList<>();
    }

    // Getter und Setter
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }
    public List<Inventories> getInventories() {
        return inventories;
    }
    public void setInventories(List<Inventories> inventories) {
        this.inventories = inventories;
    }
    public void addInventory(Inventories inventory) {
        this.inventories.add(inventory);
        inventory.setCostCenters(this);
    }
    public void removeInventory(Inventories inventory) {
        this.inventories.remove(inventory);
        inventory.setCostCenters(null);
    }
}