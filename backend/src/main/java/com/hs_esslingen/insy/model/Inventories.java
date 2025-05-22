package com.hs_esslingen.insy.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Table(name = "inventories")
public class Inventories {

    @Id
    @Column(nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cost_centers_id", nullable = true)
    @JsonIgnoreProperties("inventories")
    private CostCenters costCenters;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    @JsonIgnoreProperties({ "inventories", "histories", "comments" })
    private Users user;

    @ManyToOne
    @JoinColumn(name = "companies_id", nullable = false)
    @JsonIgnoreProperties("inventories")
    private Companies company;

    @Column(nullable = false)
    private String description;

    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    @Column(name = "is_deinventoried", nullable = false)
    @Builder.Default
    private Boolean isDeinventoried = false;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = true)
    private String location;

    @Column(name = "created_at", nullable = false)
    private final OffsetDateTime createdAt = OffsetDateTime.now(ZoneId.of("Europe/Berlin"));

    @Column(name = "deleted_at")
    @Builder.Default
    private OffsetTime deletedAt = null;

    @OneToMany(mappedBy = "inventories", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @JsonIgnore
    private List<Comments> comments = new ArrayList<>();

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @JsonIgnore
    private List<Extensions> extensions = new ArrayList<>();

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @JsonIgnore
    private Set<InventoryTagRelations> tagRelations = new HashSet<>();

    // Konstruktor
    @Builder
    public Inventories(Integer id, CostCenters costCenters, Users user, Companies company, String description,
            String serialNumber, BigDecimal price, String location) {
        this.id = id;
        this.isDeinventoried = false;
        this.costCenters = costCenters;
        this.user = user;
        this.company = company;
        this.description = description;
        this.serialNumber = serialNumber;
        this.price = price;
        this.location = location;
        this.extensions = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.tagRelations = new HashSet<>();
    }

    // Getter und Setter

    public void addComment(Comments comment) {
        if (!this.comments.contains(comment)) {
            this.comments.add(comment);
            comment.setInventories(this);
        }
    }

    public void removeComment(Comments comment) {
        if (this.comments.remove(comment)) {
            comment.setInventories(null);
        }
    }

    public void addExtension(Extensions extension) {
        if (!this.extensions.contains(extension)) {
            this.extensions.add(extension);
            extension.setInventory(this);
        }
    }

    public void removeExtension(Extensions extension) {
        if (this.extensions.remove(extension)) {
            extension.setInventory(this);
        }
    }

    public void addTagRelation(InventoryTagRelations tagRelation) {
        if (this.tagRelations.add(tagRelation)) {
            tagRelation.setInventory(this);
        }
    }

    public void removeTagRelation(InventoryTagRelations tagRelation) {
        if (this.tagRelations.remove(tagRelation)) {
            tagRelation.setInventory(null);
        }
    }

    public void delete() {
        this.deletedAt = OffsetTime.now();
        this.isDeinventoried = true;
    }
}