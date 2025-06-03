package com.hs_esslingen.insy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hs_esslingen.insy.model.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.author WHERE c.inventories.id = :inventoryId")
    List<Comment> findCommentsByInventoryId(@Param("inventoryId") Integer inventoryId);


    @Query("SELECT c FROM Comment c WHERE c.id = :commentId AND c.inventories.id = :inventoryId")
    Optional<Comment> findByCommentIdAndInventoryId(@Param("commentId") Integer commentId, @Param("inventoryId") Integer inventoryId);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.id = :commentId")
    void deleteByCommentId(@Param("commentId") Integer commentId);
}
