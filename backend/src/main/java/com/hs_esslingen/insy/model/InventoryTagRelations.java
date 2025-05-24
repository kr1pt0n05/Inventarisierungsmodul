package com.hs_esslingen.insy.model;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hs_esslingen.insy.configuration.InventoryTagKey;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inventory_tag_relations")
public class InventoryTagRelations {

    @EmbeddedId
    @Builder.Default
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
    @Builder.Default
    private final OffsetDateTime createdAt = OffsetDateTime.now(ZoneId.of("Europe/Berlin"));
}
