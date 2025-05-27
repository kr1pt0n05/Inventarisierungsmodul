package com.hs_esslingen.insy.services;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.hs_esslingen.insy.dto.Comment;
import com.hs_esslingen.insy.model.Inventories;
import com.hs_esslingen.insy.repository.InventoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.model.Comments;
import com.hs_esslingen.insy.repository.CommentsRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentsService {

    @Autowired
    private CommentsRepository commentsRepository;
    @Autowired
    private InventoriesRepository inventoriesRepository;

    public List<Comment> getCommentsByInventoryId(Integer inventoryId) {

        // Get comments from repository
        List<Comments> comments = commentsRepository.findCommentsByInventoryId(inventoryId);

        // Convert to DTOs
        return comments.stream()
                .map(comment -> Comment.builder()
                        .id(comment.getId())
                        .description(comment.getDescription())
                        .author(comment.getAuthor() != null ? comment.getAuthor().getName() : "Unknown")
                        .build())
                .collect(Collectors.toList());
    }

    public Comment createComment(Integer inventoryId, Comment commentDTO) {
        Optional<Inventories> inventory = inventoriesRepository.findById(inventoryId);
        if (inventory.isEmpty()) {
            throw new IllegalArgumentException("Inventory not found");
        }
        Comments comment = Comments.builder()
                .inventories(inventory.get())
                .description(commentDTO.getDescription())
                .author(inventory.get().getUser())
                .build();

        // Save and convert back to DTO
        Comments savedComment = commentsRepository.save(comment);

        return Comment.builder()
                .id(savedComment.getId())
                .description(savedComment.getDescription())
                .author(savedComment.getAuthor().getName())
                .build();
    }

    @Transactional
    public void deleteComment(Integer inventoryId, Integer commentId) {
        // Find the comment and verify it belongs to the inventory in one query
        Optional<Comments> commentOpt = commentsRepository.findByCommentIdAndInventoryId(commentId, inventoryId);

        if (commentOpt.isEmpty()) {
            throw new IllegalArgumentException("Comment not found or does not belong to the specified inventory");
        }

        // Delete the specific comment
        commentsRepository.deleteById(commentId);
    }

}
