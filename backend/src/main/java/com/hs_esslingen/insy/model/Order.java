package com.hs_esslingen.insy.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;


import jakarta.persistence.CascadeType;
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
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private String company;

    @Column(name = "besy_id", nullable = false)
    private Integer besyId;

    @Column(name = "deleted_at")
    @Builder.Default
    private LocalDateTime deletedAt = null;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "user_name")
    private String user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    @JsonManagedReference
    private List<Article> articles = new ArrayList<>();

    // Konstruktor
    @Builder
    public Order(String description, BigDecimal price, String company, LocalDateTime createdAt,
            String user, Integer besyId) {
        this.description = description;
        this.price = price;
        this.company = company;
        this.createdAt = createdAt;
        this.user = user;
        this.besyId = besyId;
        this.deletedAt = null;
        this.articles = new ArrayList<>();
    }

    // Getter und Setter
    public void addArticle(Article article) {
        this.articles.add(article); // Add the article to the list of articles
        article.setOrder(this); // Set the order for the article
    }

    public void removeArticle(Article article) {
        this.articles.remove(article); // Remove the article from the list of articles
        article.setOrder(null); // Set the order for the article to null
    }
}
