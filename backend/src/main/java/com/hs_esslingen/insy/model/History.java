package com.hs_esslingen.insy.model;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "histories")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "author_user_id", referencedColumnName = "id", nullable = true)
    @JsonBackReference
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Inventory inventory;

    @Column(name = "attribute_changed", nullable = false)
    private String attributeChanged;

    @Column(nullable = false)
    private String valueFrom;

    @Column(nullable = false)
    private String valueTo;

    @Column(nullable = false)
    private final LocalDateTime createdAt = LocalDateTime.now(ZoneId.of("Europe/Berlin"));

    // Konstruktor
    @Builder
    public History(User users, String attributeChanged, String from, String to) {
        this.author = users;
        this.attributeChanged = attributeChanged;
        this.valueFrom = from;
        this.valueTo = to;
    }
}