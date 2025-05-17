package com.hs_esslingen.insy.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "companies")
public class Companies {
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Inventories> inventories;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Extensions> extensions;

    // Konstruktor
    public Companies() {
        this.inventories = new ArrayList<>();
        this.extensions = new ArrayList<>();
    }

    public Companies(String name) {
        this.name = name;
        this.inventories = new ArrayList<>();
        this.extensions = new ArrayList<>();
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
    public List<Inventories> getInventories() {
        return inventories;
    }
    public void setInventories(List<Inventories> inventories) {
        this.inventories = inventories;
    }
    public void addInventory(Inventories inventory) {
        this.inventories.add(inventory);
        inventory.setCompany(this);
    }
    public void removeInventory(Inventories inventory) {
        this.inventories.remove(inventory);
        inventory.setCompany(null);
    }
    public List<Extensions> getExtensions() {
        return extensions;
    }
    public void setExtensions(List<Extensions> extensions) {
        this.extensions = extensions;
    }
    public void addExtension(Extensions extension) {
        this.extensions.add(extension);
        extension.setCompany(this);
    }
    public void removeExtension(Extensions extension) {
        this.extensions.remove(extension);
        extension.setCompany(null);
    }

}