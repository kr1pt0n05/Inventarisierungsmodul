package com.hs_esslingen.insy.configuration;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.hs_esslingen.insy.model.Inventories;
import com.hs_esslingen.insy.model.Tags;

// Klasse zur Implementierung von Filterfunktionen für die Inventargegnstände
// Filter werden als Query-Parameter in der URL übergeben
// und in der InventoriesController-Klasse verarbeitet
public class InventorySpecification {

    private InventorySpecification() {
        // Privater Konstruktor, um Instanziierung zu verhindern
    }

    // Filter Inventargegenstände nach ihrer Tag-ID
    public static Specification<Inventories> hasTagId(List<Integer> tagIds) {
    return (root, query, cb) -> {
        if (tagIds == null || tagIds.isEmpty()) {
            return cb.conjunction();
        }
        // Direkt auf das ManyToMany Set "tags" joinen
        Join<Inventories, Tags> tagJoin = root.join("tags", JoinType.LEFT);
        return tagJoin.get("id").in(tagIds);
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

    public static Specification<Inventories> hasOrderer(String orderer) {
        return (root, query, cb) -> {
            // Wenn kein Orderer gesetzt ist keinen Filter anwenden
            if (orderer == null || orderer.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("user").get("name"), orderer);
        };
    }

    public static Specification<Inventories> hasCompany(String company) {
        return (root, query, cb) -> {
            // Wenn kein Unternehmen gesetzt ist keinen Filter anwenden
            if (company == null || company.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("company").get("name"), company);
        };
    }

    public static Specification<Inventories> hasLocation(String location) {
        return (root, query, cb) -> {
            // Wenn kein Standort gesetzt ist keinen Filter anwenden
            if (location == null || location.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("location"), location);
        };
    }

    public static Specification<Inventories> hasCostCenter(String costCenter) {
        return (root, query, cb) -> {
            // Wenn kein Kostenstelle gesetzt ist keinen Filter anwenden
            if (costCenter == null || costCenter.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("costCenter").get("description"), costCenter);
        };
    }

    public static Specification<Inventories> hasSerialNumber(String serialNumber) {
        return (root, query, cb) -> {
            // Wenn keine Seriennummer gesetzt ist keinen Filter anwenden
            if (serialNumber == null || serialNumber.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("serialNumber"), serialNumber);
        };
    }

    // Sortierung nach einem verschachtelten Feld
    // Beispiel: "user.name" sortiert nach dem Namen des Benutzers
    public static Specification<Inventories> sortByNestedField(String nestedField, Sort.Direction direction) {
    return (root, query, cb) -> {

        // Wenn kein verschachteltes Feld angegeben ist, keine Sortierung anwenden
        // Eigentlich schon überprüft in InventoriesSerivice,
        // aber hier nochmal für Sicherheit
        if (nestedField == null || !nestedField.contains(".")) {
            return cb.conjunction(); // keine Sortierung
        }

        // Das verschachtelte Feld in zwei Teile aufteilen
        // z. B. "user.name"
        String[] parts = nestedField.split("\\.");
        if (parts.length != 2) return cb.conjunction();

        // Tabellenname
        String joinProperty = parts[0]; // z. B. "user"
        // Property, nach dem sortiert werden soll
        String sortField = parts[1];   // z. B. "name"

        // Join auf die Tabelle des verschachtelten Feldes
        // und Sortierung nach dem angegebenen Feld
        Join<Object, Object> join = root.join(joinProperty, JoinType.LEFT);
        query.orderBy(direction == Sort.Direction.ASC
            ? cb.asc(join.get(sortField))
            : cb.desc(join.get(sortField)));

        return cb.conjunction(); // nur OrderBy hinzufügen, keine Filterbedingung
    };
}

    // Filter nach dem Erstellungsdatum
    public static Specification<Inventories> createdBetween(LocalDateTime createdAfter, LocalDateTime createdBefore) {
    return (root, query, cb) -> {
        if (createdAfter == null && createdBefore == null) {
            return cb.conjunction();
        } else if (createdAfter != null && createdBefore != null) {
            return cb.between(root.get("createdAt"), createdAfter, createdBefore);
        } else if (createdAfter != null) {
            return cb.greaterThanOrEqualTo(root.get("createdAt"), createdAfter);
        } else {
            return cb.lessThanOrEqualTo(root.get("createdAt"), createdBefore);
        }
    };
}

}

