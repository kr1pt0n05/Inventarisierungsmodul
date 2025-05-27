package com.hs_esslingen.insy.utils;
import java.util.Set;

// Utility class for managing allowed fields for the orderBy-field in queries
public class OrderByUtils {

    public static final Set<String> ALLOWED_ORDER_BY_FIELDS = Set.of(
        "id",
        "description",
        "company.name",
        "price",
        "createdAt",
        "serialNumber",
        "location",
        "user.name"
    );

    // Private constructor to prevent instantiation
    private OrderByUtils() {
    }
}
