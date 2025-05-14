package com.hs_esslingen.insy.model;

import java.math.BigDecimal;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
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
     private OffsetTime deletedAt;

     @OneToMany(mappedBy = "orderId", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
     private List<Article> articles;

     // Konstruktor
        public Orders() {
            this.articles = new ArrayList<>();
        }
        public Orders(Integer id, String description, BigDecimal price, String company, OffsetTime ordered, Integer besyId) {
            this.id = id;
            this.description = description;
            this.price = price;
            this.company = company;
            this.ordered = ordered;
            this.besyId = besyId;
            this.deletedAt = null; // Standardwert für deletedAt
            this.articles = new ArrayList<>();
        }

     // Getter und Setter
        public Integer getId() {
            return id;
        }    
        public void setId(Integer id) {
            this.id = id;
        }

        public Orders getOrder() {
            return Orders.this;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }   
        public String getCompany() {
            return company;
        }  

        public void setCompany(String company) {
            this.company = company;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public OffsetTime getOrdered() {
            return ordered;
        }

        public void setOrdered(OffsetTime ordered) {
            this.ordered = ordered;
        }

        public Integer getBesyId() {
            return besyId;
        }

        public void setBesyId(Integer besyId) {
            this.besyId = besyId;
        }

        public OffsetTime getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(OffsetTime deletedAt) {
            this.deletedAt = deletedAt;
        }

        public List<Article> getArticles() {
            return articles;
        }

        public void setArticles(List<Article> articles) {
            this.articles = articles;
        }
        public void addArticle(Article article) {
            this.articles.add(article); // Add the article to the list of articles
            article.setOrderId(this); // Set the order for the article
        }

}
