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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @Builder.Default
    @JsonManagedReference
    private List<Inventory> inventories = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @Builder.Default
    @JsonManagedReference
    private List<Extension> extensions = new ArrayList<>();

    @Builder
    public Company(String name) {
        this.name = name;
        this.inventories = new ArrayList<>();
        this.extensions = new ArrayList<>();
    }

    // Getter und Setter
    public void addInventory(Inventory inventory) {
        this.inventories.add(inventory);
        inventory.setCompany(this);
    }

    public void removeInventory(Inventory inventory) {
        this.inventories.remove(inventory);
        inventory.setCompany(null);
    }

    public void addExtension(Extension extension) {
        this.extensions.add(extension);
        extension.setCompany(this);
    }

    public void removeExtension(Extension extension) {
        this.extensions.remove(extension);
        extension.setCompany(null);
    }
}