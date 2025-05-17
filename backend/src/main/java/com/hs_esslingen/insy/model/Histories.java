package com.hs_esslingen.insy.model;

import java.time.OffsetTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
    @JsonBackReference
    private Users author;

    @Column(name = "attribute_changed", nullable = false)
    private String attributeChanged;

    @Column(nullable = false)
    private String valueFrom;

    @Column(nullable = false)
    private String valueTo;

    @Column(nullable = false)
    private OffsetTime date;

    // Konstruktor
    public Histories() {
    }

    public Histories(Users users, String attributeChanged, String from, String to) {
        this.author = users;
        this.attributeChanged = attributeChanged;
        this.valueFrom = from;
        this.valueTo = to;
        this.date = OffsetTime.now();
    }

    // Getter und Setter

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Users getAuthor() {
        return author;
    }

    public void setAuthor(Users users) {
        this.author = users;
    }

    public String getAttributeChanged() {
        return attributeChanged;
    }

    public void setAttributeChanged(String attributeChanged) {
        this.attributeChanged = attributeChanged;
    }

    public String getValueFrom() {
        return valueFrom;
    }

    public void setValueFrom(String from) {
        this.valueFrom = from;
    }

    public String getValueTo() {
        return valueTo;
    }

    public void setValueTo(String to) {
        this.valueTo = to;
    }

    public OffsetTime getDate() {
        return date;
    }
}