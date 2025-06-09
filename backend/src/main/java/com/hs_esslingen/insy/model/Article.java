package com.hs_esslingen.insy.model;

import java.math.BigDecimal; // für monetary values

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
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
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order orderId;

    @Column(name = "description")
    private String description;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "price")
    private BigDecimal price;
}