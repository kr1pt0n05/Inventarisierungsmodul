package com.hs_esslingen.insy.model;

import java.time.OffsetDateTime;
import java.time.ZoneId;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@Table(name = "comments")
public class Comments {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = true)
    @JsonBackReference
    private Inventories inventories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_user_id", referencedColumnName = "id", nullable = true)
    @JsonBackReference
    private Users author;

    @Column(nullable = false)
    private String description;

    @Column(name = "created_at", nullable = false)
    private final OffsetDateTime createdAt = OffsetDateTime.now(ZoneId.of("Europe/Berlin"));

    @Builder
    public Comments(Inventories inventories, Users users, String description) {
        this.inventories = inventories;
        this.author = users;
        this.description = description;
    }
}
