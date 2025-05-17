package com.hs_esslingen.insy.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hs_esslingen.insy.configuration.InventoryTagKey;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory_tag_relations")
public class InventoryTagRelations {
    

    @EmbeddedId
    private InventoryTagKey id = new InventoryTagKey();

    @ManyToOne
    @MapsId("inventoryId")
    @JoinColumn(name = "inventory_id")
    @JsonBackReference
    private Inventories inventory;

    @ManyToOne
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    @JsonBackReference
    private Tags tag;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Konstruktor
    public InventoryTagRelations() {
    }
    public InventoryTagRelations(Inventories inventory, Tags tag) {
        this.inventory = inventory;
        this.tag = tag;
        this.createdAt = LocalDateTime.now();
    }

    // Getter und Setter
    public InventoryTagKey getId() {
        return id;
    }
    public void setId(InventoryTagKey id) {
        this.id = id;
    }

    public Inventories getInventory() {
        return inventory;
    }

    public void setInventory(Inventories inventory) {
        this.inventory = inventory;
    }

    public Tags getTag() {
        return tag;
    }

    public void setTag(Tags tag) {
        this.tag = tag;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryTagRelations that = (InventoryTagRelations) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
