package com.hs_esslingen.insy.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = "inventories") // Entity-Name in Einzahl
public class Inventory {

    @Id
    @EqualsAndHashCode.Include
    @Column(nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cost_centers_id", nullable = true)
    private CostCenter costCenter;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "companies_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    private String description;

    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    @Column(name = "is_deinventoried", nullable = false)
    @Builder.Default
    private Boolean isDeinventoried = false; // ToDo: umwandeln in boolean, da es nur zwei Zustände gibt, das Objekt
                                             // schließt aber null nicht aus

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = true)
    private String location;

    @Column(name = "created_at", nullable = false)
    private final LocalDateTime createdAt = LocalDateTime.now(ZoneId.of("Europe/Berlin"));

    @Column(name = "deleted_at")
    @Builder.Default
    private LocalDateTime deletedAt = null;

    @OneToMany(mappedBy = "inventories", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Extension> extensions = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "inventory_tag", joinColumns = @JoinColumn(name = "inventory_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    // Konstruktor
    @Builder
    public Inventory(Integer id, CostCenter costCenters, User user, Company company, String description,
            String serialNumber, BigDecimal price, String location) {
        this.id = id;
        this.isDeinventoried = false;
        this.costCenter = costCenters;
        this.user = user;
        this.company = company;
        this.description = description;
        this.serialNumber = serialNumber;
        this.price = price;
        this.location = location;
        this.extensions = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.tags = new HashSet<>();
    }

    // Getter und Setter

    public void addComment(Comment comment) {
        if (!this.comments.contains(comment)) {
            this.comments.add(comment);
            comment.setInventories(this);
        }
    }

    public void removeComment(Comment comment) {
        if (this.comments.remove(comment)) {
            comment.setInventories(null);
        }
    }

    // Adds an extension to the inventory and updates the price accordingly
    public void addExtension(Extension extension) {
        if (!this.extensions.contains(extension)) {
            this.extensions.add(extension);

            extension.setInventory(this);

            if (this.price == null) {
                this.price = BigDecimal.ZERO;
            }
            if (extension.getPrice() != null) {
                this.price = this.price.add(extension.getPrice());
            }
        }
    }

    // Removes an extension from the inventory and updates the price accordingly
    public void removeExtension(Extension extension) {
        if (this.extensions.remove(extension)) {
            extension.setInventory(null);

            if (this.price != null && extension.getPrice() != null) {
                this.price = this.price.subtract(extension.getPrice());
            }
        }
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
        this.isDeinventoried = true;
    }

}