package com.hs_esslingen.insy.model;

import java.time.OffsetTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name = "histories")
public class Histories {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "author_user_id", referencedColumnName = "id", nullable = false)
    private Users users;

    @Column(name = "attribute_changed", nullable = false)
    private String attributeChanged;

    @Column(nullable = false)
    private String from;

    @Column(nullable = false)
    private String to;

    @Column(nullable = false)
    private OffsetTime date;

    // Konstruktor
    public Histories() {
    }

    public Histories(Users users, String attributeChanged, String from, String to) {
        this.users = users;
        this.attributeChanged = attributeChanged;
        this.from = from;
        this.to = to;
        this.date = OffsetTime.now();
    }

    // Getter und Setter

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public String getAttributeChanged() {
        return attributeChanged;
    }

    public void setAttributeChanged(String attributeChanged) {
        this.attributeChanged = attributeChanged;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public OffsetTime getDate() {
        return date;
    }
}