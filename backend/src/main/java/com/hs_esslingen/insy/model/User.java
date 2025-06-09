package com.hs_esslingen.insy.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    @Builder.Default
    private List<Inventory> inventories = new ArrayList<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    @JsonManagedReference
    @Builder.Default
    private List<History> histories = new ArrayList<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    @JsonManagedReference
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    /*
     * Auskommentiert zu Testzwecken
     * 
     * @Column(name = "keycloak_id", nullable = false)
     * private final String keycloakID;
     */

    @Builder
    public User(String name) {
        this.name = name;
        this.inventories = new ArrayList<>();
        this.histories = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public void addInventory(Inventory inventory) {
        this.inventories.add(inventory);
        inventory.setUser(this);
    }

    public void removeInventory(Inventory inventory) {
        this.inventories.remove(inventory);
        inventory.setUser(null);
    }

    public void addHistory(History history) {
        this.histories.add(history);
        history.setAuthor(this);
    }

    public void removeHistory(History history) {
        this.histories.remove(history);
        history.setAuthor(null);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setAuthor(this);
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setAuthor(this);
    }
}
