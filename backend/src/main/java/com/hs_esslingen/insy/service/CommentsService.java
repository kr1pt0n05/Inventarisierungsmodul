package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.hs_esslingen.insy.model.Inventories;
import com.hs_esslingen.insy.repository.InventoriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.dto.Comment;
import com.hs_esslingen.insy.model.Comments;
import com.hs_esslingen.insy.repository.CommentsRepository;
import org.springframework.transaction.annotation.Transactional;
@RequiredArgsConstructor
@Service
public class CommentsService {

    private final CommentsRepository commentsRepository;
    private final InventoriesRepository inventoriesRepository;


    public List<Comment> getCommentsByInventoryId(Integer inventoryId) {
        Optional<Inventories> inventory = inventoriesRepository.findById(inventoryId);

        if (inventory.isEmpty()) {
            throw new IllegalArgumentException("Inventory with the id: " + inventoryId + " not found");
        }

        // Get comments from repository
        List<Comments> comments = commentsRepository.findCommentsByInventoryId(inventoryId);

        // Convert to DTOs
        return comments.stream()
                .map(comment -> Comment.builder()
                        .id(comment.getId())
                        .description(comment.getDescription())
                        .author(comment.getAuthor() != null ? comment.getAuthor().getName() : "Unknown")
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public Comment createComment(Integer inventoryId, Comment commentDTO) {
        Optional<Inventories> inventory = inventoriesRepository.findById(inventoryId);
        if (inventory.isEmpty()) {
            throw new IllegalArgumentException("Inventory with the id: " + inventoryId + " not found");
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
                .createdAt(savedComment.getCreatedAt())
                .author(savedComment.getAuthor().getName())
                .build();
    }

    @Transactional
    public void deleteComment(Integer inventoryId, Integer commentId) {
        // Verify that the inventory with the given id exists
        Optional<Inventories> inventory = inventoriesRepository.findById(inventoryId);

        if (inventory.isEmpty()) {
            throw new IllegalArgumentException("Inventory with the id: " + inventoryId + " not found");
        }
        // Verify that the comment belongs to the given inventoryId
        Optional<Comments> commentOpt = commentsRepository.findByCommentIdAndInventoryId(commentId, inventoryId);
        if (commentOpt.isEmpty()) {
            throw new IllegalArgumentException("Comment not found or does not belong to the specified inventory with the id: " + inventoryId);
        } else {
            // Delete the specific comment
            commentsRepository.deleteByCommentId(commentId);
        }
    }
}
