package com.hs_esslingen.insy.configuration;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.hs_esslingen.insy.model.Inventories;
import com.hs_esslingen.insy.model.InventoryTagRelations;

// Klasse zur Implementierung von Filterfunktionen für die Inventargegnstände
// Filter werden als Query-Parameter in der URL übergeben
// und in der InventoriesController-Klasse verarbeitet
public class InventorySpecification {

    // Filter Inventargegenstände nach ihrer Tag-ID
    public static Specification<Inventories> hasTagId(List<Integer> tagIds) {
        return (root, query, cb) -> {
            // Wenn keine Tag-ID gesetzt ist keinen Filter anwenden
            if (tagIds == null || tagIds.isEmpty()) {
                return cb.conjunction();
            }
            Join<Inventories, InventoryTagRelations> tagJoin = root.join("tagRelations", JoinType.LEFT);
            return tagJoin.get("tag").get("id").in(tagIds);
        };
    }


    public static Specification<Inventories> idBetween(Integer minId, Integer maxId) {
        return (root, query, cb) -> {
            // Wenn keine ID gesetzt ist keinen Filter anwenden
            if(minId == null && maxId == null) {
                return cb.conjunction();
            }
            else if (minId != null && maxId != null) {
                return cb.between(root.get("id"), minId, maxId);
            } else if (minId != null) {
                return cb.greaterThanOrEqualTo(root.get("id"), minId);
            } else if (maxId != null) {
                return cb.lessThanOrEqualTo(root.get("id"), maxId);
            }
            return cb.conjunction();
        };
    }

    public static Specification<Inventories> priceBetween(Integer minPrice, Integer maxPrice) {
        return (root, query, cb) -> {
            // Wenn keine Query-Parameter gesetzt sind keinen Filter anwenden
            if(minPrice == null && maxPrice == null) {
                return cb.conjunction();
            }
            else if (minPrice != null && maxPrice != null) {
                return cb.between(root.get("price"), BigDecimal.valueOf(minPrice), BigDecimal.valueOf(maxPrice));
            } else if (minPrice != null) {
                return cb.greaterThanOrEqualTo(root.get("price"), BigDecimal.valueOf(minPrice));
            } else if (maxPrice != null) {
                return cb.lessThanOrEqualTo(root.get("price"), BigDecimal.valueOf(maxPrice));
            }
            return cb.conjunction();
        };
    }

    public static Specification<Inventories> isDeinventoried(Boolean status) {
        return (root, query, criteriaBuilder) ->
            status == null ? null : criteriaBuilder.equal(root.get("isDeinventoried"), status);
    }
}

