package com.hs_esslingen.insy.model;

import java.math.BigDecimal;
import java.time.OffsetTime;
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
@Table(name = "orders")
public class Orders {

    @Id
    private Integer id;

    private String description;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private String company;

    private OffsetTime ordered;

    @Column(name = "besy_id", nullable = false)
    private Integer besyId;

    @Column(name = "deleted_at")
    @Builder.Default
    private OffsetTime deletedAt = null;

    @OneToMany(mappedBy = "orderId", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    @JsonManagedReference
    private List<Article> articles = new ArrayList<>();

    // Konstruktor
    @Builder
    public Orders(Integer id, String description, BigDecimal price, String company, OffsetTime ordered,
            Integer besyId) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.company = company;
        this.ordered = ordered;
        this.besyId = besyId;
        this.deletedAt = null;
        this.articles = new ArrayList<>();
    }

    // Getter und Setter
    public void addArticle(Article article) {
        this.articles.add(article); // Add the article to the list of articles
        article.setOrderId(this); // Set the order for the article
    }

    public void removeArticle(Article article) {
        this.articles.remove(article); // Remove the article from the list of articles
        article.setOrderId(null); // Set the order for the article to null
    }
}
