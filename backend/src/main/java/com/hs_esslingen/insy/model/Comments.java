package com.hs_esslingen.insy.model;

import java.time.OffsetTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "comments")
public class Comments {
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "inventory_id", nullable = true)
    @JsonBackReference
    private Inventories inventories;

    @ManyToOne
    @JoinColumn(name = "author_user_id", referencedColumnName = "id", nullable = true)
    @JsonBackReference
    private Users author;

    @Column(nullable = false)
    private String description;

    @Column(name = "created_at", nullable = false)
    private OffsetTime createdAt;

    public Comments() {
    }

    public Comments(Inventories inventories, Users users, String description) {
        this.inventories = inventories;
        this.author = users;
        this.description = description;
        this.createdAt = OffsetTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Inventories getInventories() {
        return inventories;
    }

    public void setInventories(Inventories inventories) {
        this.inventories = inventories;
    }

    public Users getUsers() {
        return author;
    }

    public void setUsers(Users users) {
        this.author = users;
    }

    public String getDescription() {
        return description;
    }

    public OffsetTime getCreatedAt() {
        return createdAt;
    }

    public Users getAuthor() {
        return author;
    }

    public void setAuthor(Users author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(OffsetTime createdAt) {
        this.createdAt = createdAt;
    }
}
