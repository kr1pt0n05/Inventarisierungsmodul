package com.hs_esslingen.insy.configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.hs_esslingen.insy.exception.BadRequestException;
import com.hs_esslingen.insy.model.Inventory;
import com.hs_esslingen.insy.model.Tag;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

// Class for implementing filter function for inventory items
// Filter are passed as a Query-Parameter in the URL
// and processed in the InventoriesController class
public class InventorySpecification {

    private InventorySpecification() {
        // Private Constructor to prevent instantiation
    }

    // Filter inventory items by their Tag-ID
    public static Specification<Inventory> hasTagId(List<Integer> tagIds) {
        return (root, query, cb) -> {
            if (tagIds == null || tagIds.isEmpty()) {
                return cb.conjunction();
            }
            // Join directly to the ManyToMany set "tags"
            Join<Inventory, Tag> tagJoin = root.join("tags", JoinType.LEFT);
            return tagJoin.get("id").in(tagIds);
        };
    }

    public static Specification<Inventory> idBetween(Integer minId, Integer maxId) {
        return (root, query, cb) -> {
            // If the ID is not set don't use any filter
            if (minId == null && maxId == null) {
                return cb.conjunction();
            } else if (minId != null && maxId != null) {
                return cb.between(root.get("id"), minId, maxId);
            } else if (minId != null) {
                return cb.greaterThanOrEqualTo(root.get("id"), minId);
            } else if (maxId != null) {
                return cb.lessThanOrEqualTo(root.get("id"), maxId);
            }
            return cb.conjunction();
        };
    }

    public static Specification<Inventory> priceBetween(Integer minPrice, Integer maxPrice) {
        return (root, query, cb) -> {
            // If Query-Parameters are not set don't use any filter
            if (minPrice == null && maxPrice == null) {
                return cb.conjunction();
            } else if (minPrice != null && maxPrice != null) {
                return cb.between(root.get("price"), BigDecimal.valueOf(minPrice), BigDecimal.valueOf(maxPrice));
            } else if (minPrice != null) {
                return cb.greaterThanOrEqualTo(root.get("price"), BigDecimal.valueOf(minPrice));
            } else if (maxPrice != null) {
                return cb.lessThanOrEqualTo(root.get("price"), BigDecimal.valueOf(maxPrice));
            }
            return cb.conjunction();
        };
    }

    public static Specification<Inventory> isDeinventoried(Boolean status) {
        return (root, query, criteriaBuilder) -> status == null ? null
                : criteriaBuilder.equal(root.get("isDeinventoried"), status);
    }

    public static Specification<Inventory> hasOrderer(String orderer) {
        return (root, query, cb) -> {
            // If orderer is not set don't use any filter
            if (orderer == null || orderer.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("user").get("name"), orderer);
        };
    }


    public static Specification<Inventory> hasOrderers(List<String> orderers) {
        return (root, query, cb) -> {
            if (orderers == null || orderers.isEmpty()) {
                return cb.conjunction();
            }
            return root.get("user").get("name").in(orderers);
        };
    }


    public static Specification<Inventory> hasCompany(String company) {
        return (root, query, cb) -> {
            // If company is not set don't use any filter
            if (company == null || company.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("company").get("name"), company);
        };
    }


    public static Specification<Inventory> hasCompanies(List<String> companies) {
        return (root, query, cb) -> {
            if (companies == null || companies.isEmpty()) {
                return cb.conjunction();
            }
            return root.get("company").get("name").in(companies);
        };
    }

    public static Specification<Inventory> hasLocation(String location) {
        return (root, query, cb) -> {
            // If location is not set don't use any filter
            if (location == null || location.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("location"), location);
        };
    }


    public static Specification<Inventory> hasLocations(List<String> locations) {
        return (root, query, cb) -> {
            if (locations == null || locations.isEmpty()) {
                return cb.conjunction();
            }
            return root.get("location").in(locations);
        };
    }

    public static Specification<Inventory> hasCostCenter(String costCenter) {
        return (root, query, cb) -> {
            // If costCenter is not set don't use any filter
            if (costCenter == null || costCenter.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("costCenter").get("description"), costCenter);
        };
    }


    public static Specification<Inventory> hasCostCenters(List<String> costCenters) {
        return (root, query, cb) -> {
            if (costCenters == null || costCenters.isEmpty()) {
                return cb.conjunction();
            }
            return root.get("costCenter").get("description").in(costCenters);
        };
    }

    public static Specification<Inventory> hasSerialNumber(String serialNumber) {
        return (root, query, cb) -> {
            // If serialNumber ist not set don't use any filter
            if (serialNumber == null || serialNumber.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("serialNumber"), serialNumber);
        };
    }


    public static Specification<Inventory> hasSerialNumbers(List<String> serialNumbers) {
        return (root, query, cb) -> {
            if (serialNumbers == null || serialNumbers.isEmpty()) {
                return cb.conjunction();
            }
            return root.get("serialNumber").in(serialNumbers);
        };
    }

    // Sort by nested field (e.g. "user.name" sorts by user's name)
    public static Specification<Inventory> sortByNestedField(String orderBy, Sort.Direction direction) {
        return (root, query, cb) -> {

            // If no nested field is specified, do not apply sorting
            // It's already checked in InventoriesService but here again for security
            if (orderBy == null) {
                return cb.conjunction(); // no sorting
            }

            // Table name
            String joinProperty = orderBy; // z. B. "user"

            // Property to sort by
            // Set the sort field based on the joinProperty
            // This is the field in the joined table that we want to sort by
            String sortField;
            switch (joinProperty) {
                case "user":
                    sortField = "name"; // Sort by user's name
                    break;
                case "company":
                    sortField = "name"; // Sort by company's name
                    break;
                case "costCenter":
                    sortField = "description"; // Sort by cost center's description
                    break;
                default:
                    throw new BadRequestException("Invalid orderBy field: " + orderBy);
            }

            // Join on the table of the nested field and sort by the specified field
            Join<Object, Object> join = root.join(joinProperty, JoinType.LEFT);
            query.orderBy(direction == Sort.Direction.ASC
                    ? cb.asc(join.get(sortField))
                    : cb.desc(join.get(sortField)));

            return cb.conjunction(); // Add only OrderBy, no filters
        };
    }

    // Filter by creation date
    public static Specification<Inventory> createdBetween(LocalDateTime createdAfter, LocalDateTime createdBefore) {
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

    // Filter by search text that can appear in multiple fields
    public static Specification<Inventory> hasSearchText(String searchText) {
        return (root, query, cb) -> {
            if (searchText != null && !searchText.isBlank()) {
                return cb.like(cb.lower(root.get("searchText")), "%" + searchText.toLowerCase() + "%");
            }
            return cb.conjunction();
        };
    }

}
