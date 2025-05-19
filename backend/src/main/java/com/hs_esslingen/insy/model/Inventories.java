package com.hs_esslingen.insy.model;

import java.math.BigDecimal;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventories")
public class Inventories {

    @Id
    @Column(nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cost_centers_id", nullable = false)
    @JsonIgnoreProperties("inventories")
    private CostCenters costCenters;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    @JsonIgnoreProperties({"inventories", "histories", "comments"})
    private Users user;

    @ManyToOne
    @JoinColumn(name = "companies_id", nullable = false)
    @JsonIgnoreProperties("inventories")
    private Companies company;

    @Column(nullable = false)
    private String description;

    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    @Column(name = "is_deinventoried")
    private Boolean isDeinventoried = false;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String location;

    @Column(name = "created_at", nullable = false)
    private OffsetTime createdAt;

    @Column(name = "deleted_at")
    private OffsetTime deletedAt = null;

    @OneToMany(mappedBy = "inventories", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Comments> comments;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Extensions> extensions;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<InventoryTagRelations> tagRelations = new HashSet<>();

    // Konstruktor
    /**
     * Default constructor for the {@code Inventories} class.
     * Initializes the {@code comments} and {@code extensions} lists as empty {@code ArrayList} instances.
     */
    public Inventories() {
        this.comments = new ArrayList<>();
        this.extensions = new ArrayList<>();
        this.tagRelations = new HashSet<>();
        this.isDeinventoried = false;
     }
    public Inventories(Integer id, CostCenters costCenters, Users user, Companies company, String description, String serialNumber, BigDecimal price, String location) {
        this.id = id;
        this.isDeinventoried = false;
        this.costCenters = costCenters;
        this.user = user;
        this.company = company;
        this.description = description;
        this.serialNumber = serialNumber;
        this.price = price;
        this.location = location;
        this.createdAt = OffsetTime.now();
        this.comments = new ArrayList<>();
        this.extensions = new ArrayList<>();
        this.tagRelations = new HashSet<>();
    }

    // Getter und Setter
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public CostCenters getCostCenters() {
        return costCenters;
    }
    public void setCostCenters(CostCenters costCenters) {
        this.costCenters = costCenters;
    }
    public Users getUser() {
        return user;
    }
    public void setUser(Users user) {
        this.user = user;
    }
    public Companies getCompany() {
        return company;
    }
    public void setCompany(Companies company) {
        this.company = company;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    public Boolean getIsDeinventoried() {
        return isDeinventoried;
    }
    public void setIsDeinventoried(Boolean isDeinventoried) {
        this.isDeinventoried = isDeinventoried;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public OffsetTime getCreatedAt() {
        return createdAt;
    }
    public OffsetTime getDeletedAt() {
        return deletedAt;
    }
    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public void addComment(Comments comment) {
        this.comments.add(comment);
        comment.setInventories(this);
    }

    public void removeComment(Comments comment) {
        this.comments.remove(comment);
        comment.setInventories(null);
    }
    public void setCreatedAt(OffsetTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Extensions> getExtensions() {
        return extensions;
    }
    public void setExtensions(List<Extensions> extensions) {
        this.extensions = extensions;
    }
    public void addExtension(Extensions extension) {
        this.extensions.add(extension);
        extension.setInventories(this);
    }
    public void removeExtension(Extensions extension) {
        this.extensions.remove(extension);
        extension.setInventories(null);
    }
    public void delete() {
        this.deletedAt = OffsetTime.now();
        this.isDeinventoried = true;
    }

    public Set<InventoryTagRelations> getTagRelations() {
        return tagRelations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inventories that = (Inventories) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}