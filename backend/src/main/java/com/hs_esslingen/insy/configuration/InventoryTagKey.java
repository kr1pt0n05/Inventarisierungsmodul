package com.hs_esslingen.insy.configuration;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class InventoryTagKey implements Serializable {

    @Column(name = "inventory_id")
    private Integer inventoryId;

    @Column(name = "tag_id")
    private Integer tagId;

    // default constructor, equals & hashCode
    public InventoryTagKey() {
    }
    public InventoryTagKey(Integer inventoryId, Integer tagId) {
        this.inventoryId = inventoryId;
        this.tagId = tagId;
    }
    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }
    
}

