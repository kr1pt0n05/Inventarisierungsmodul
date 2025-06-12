package com.hs_esslingen.insy.model;

import java.math.BigDecimal; // für monetary values

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;

    @Column(name = "inventories_id")
    private Integer inventoriesId;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    private String company;

    @Column(name = "is_inventoried")
    @Builder.Default
    private Boolean isInventoried = false;

    @Column(name = "cost_center")
    private String costCenter;

    @Column(name = "user_name")
    private String user;

    @Column(name = "serial_number")
    private String serialNumber;

    private String location;

    @Column(name = "is_extension")
    private Boolean isExtension;

    @ElementCollection
    @Builder.Default
    @CollectionTable(name = "article_tags", joinColumns = @JoinColumn(name = "article_id"))
    @Column(name = "tag_id")
    private List<Integer> tags = new ArrayList<>();

}