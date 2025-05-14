package com.hs_esslingen.insy.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class Users {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;
    
    private String name;
    
    @OneToMany(mappedBy = "user")
    private List<Inventories> inventories;

    @OneToMany(mappedBy = "histories")
    private List<Histories> histories;

    @OneToMany(mappedBy = "comments")
    private List<Comments> comments;

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
        history.setUsers(this);
    }

    public void removeHistory(Histories history) {
        this.histories.remove(history);
        history.setUsers(null);
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
}
