package com.hs_esslingen.insy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hs_esslingen.insy.model.Comments;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Integer> {
    @Query("SELECT c FROM Comments c LEFT JOIN FETCH c.author WHERE c.inventories.id = :inventoryId")
    List<Comments> findCommentsByInventoryId(@Param("inventoryId") Integer inventoryId);


    @Query("SELECT c FROM Comments c WHERE c.id = :commentId AND c.inventories.id = :inventoryId")
    Optional<Comments> findByCommentIdAndInventoryId(@Param("commentId") Integer commentId, @Param("inventoryId") Integer inventoryId);
}
