package com.hs_esslingen.insy.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    @Builder.Default
    private List<Inventories> inventories = new ArrayList<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    @JsonManagedReference
    @Builder.Default
    private List<Histories> histories = new ArrayList<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    @JsonManagedReference
    @Builder.Default
    private List<Comments> comments = new ArrayList<>();

    /*
     * Auskommentiert zu Testzwecken
     * 
     * @Column(name = "keycloak_id", nullable = false)
     * private final String keycloakID;
     */

    @Builder
    public Users(String name) {
        this.name = name;
        this.inventories = new ArrayList<>();
        this.histories = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public void addInventory(Inventories inventory) {
        this.inventories.add(inventory);
        inventory.setUser(this);
    }

    public void removeInventory(Inventories inventory) {
        this.inventories.remove(inventory);
        inventory.setUser(null);
    }

    public void addHistory(Histories history) {
        this.histories.add(history);
        history.setAuthor(this);
    }

    public void removeHistory(Histories history) {
        this.histories.remove(history);
        history.setAuthor(null);
    }

    public void addComment(Comments comment) {
        this.comments.add(comment);
        comment.setAuthor(this);
    }

    public void removeComment(Comments comment) {
        this.comments.remove(comment);
        comment.setAuthor(this);
    }
}
