package com.hs_esslingen.insy.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class Users {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;
    
    private String name;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Inventories> inventories;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Histories> histories;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Comments> comments;

    //Auskommentiert zu Testzwecken
    /*@Column(name = "keycloak_id", nullable = false)
    private String keycloakID;*/

    // Konstruktor
    public Users() {
        this.inventories = new ArrayList<>();
        this.histories = new ArrayList<>();
        this.comments = new ArrayList<>();
    }
    public Users(String name) {
        this.name = name;
        this.inventories = new ArrayList<>();
        this.histories = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    //Getter und Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public List<Inventories> getInventories() {
        return inventories;
    }
    public void setInventories(List<Inventories> inventories) {
        this.inventories = inventories;
    }
    public void addInventory(Inventories inventory) {
        this.inventories.add(inventory);
        inventory.setUser(this);
    }
    public void removeInventory(Inventories inventory) {
        this.inventories.remove(inventory);
        inventory.setUser(null);
    }

    public List<Histories> getHistories() {
        return histories;
    }

    public void setHistories(List<Histories> histories) {
        this.histories = histories;
    }

    public void addHistory(Histories history) {
        this.histories.add(history);
        history.setAuthor(this);
    }

    public void removeHistory(Histories history) {
        this.histories.remove(history);
        history.setAuthor(null);
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public void addComment(Comments comment) {
        this.comments.add(comment);
        comment.setUsers(this);
    }

    public void removeComment(Comments comment) {
        this.comments.remove(comment);
        comment.setUsers(null);
    }
    //Auskommentiert zu Testzwecken
    /*public String getKeycloakID() {
        return keycloakID;
    }
    public void setKeycloakID(String keycloakID) {
        this.keycloakID = keycloakID;
    }*/
}
