package com.hs_esslingen.insy.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hs_esslingen.insy.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    
    Optional<Order> findByBesyId(Integer besyId);
    
    @Query("SELECT o FROM Order o WHERE o.deletedAt IS NULL ORDER BY o.createdAt DESC")
    List<Order> findAllByDeletedAtIsNull();
    
    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
    List<Order> findAllOrderedByCreatedAt();
    
    // Statistik-Queries
    @Query("SELECT COUNT(o) FROM Order o WHERE o.deletedAt IS NULL")
    Long countActiveOrders();
    
    @Query("SELECT COALESCE(SUM(o.price), 0) FROM Order o WHERE o.deletedAt IS NULL")
    BigDecimal sumActivePrices();
    
    @Query("SELECT o.user, COUNT(o), COALESCE(SUM(o.price), 0) " +
           "FROM Order o " +
           "WHERE o.deletedAt IS NULL AND o.user IS NOT NULL " +
           "GROUP BY o.user " +
           "ORDER BY COUNT(o) DESC")
    List<Object[]> findOrderStatisticsByUser();
}