package com.hs_esslingen.insy.utils;

import java.util.Set;

// Utility class for managing allowed fields for the orderBy-field in queries
public class OrderByUtils {

    // Set of fields that are allowed for ordering in queries
    public static final Set<String> ALLOWED_ORDER_BY_FIELDS = Set.of(
            "id",
            "description",
            "company",
            "price",
            "createdAt",
            "serialNumber",
            "location",
            "costCenter",
            "user");

    // Set of fields that are considered foreign keys and therefore need a join to
    // access
    public static final Set<String> FOREIGN_SET = Set.of(
            "company",
            "user",
            "costCenter");

    // Private constructor to prevent instantiation
    private OrderByUtils() {
    }
}
