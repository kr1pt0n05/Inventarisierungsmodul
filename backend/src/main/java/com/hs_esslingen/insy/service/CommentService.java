package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.hs_esslingen.insy.model.Inventory;
import com.hs_esslingen.insy.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.dto.CommentDTO;
import com.hs_esslingen.insy.model.Comment;
import com.hs_esslingen.insy.repository.CommentRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private final CommentRepository commentsRepository;
    private final InventoryRepository inventoriesRepository;

    CommentService(CommentRepository commentsRepository,
                          InventoryRepository inventoriesRepository) {
        this.commentsRepository = commentsRepository;
        this.inventoriesRepository = inventoriesRepository;
    }

    public List<CommentDTO> getCommentsByInventoryId(Integer inventoryId) {

        // Get comments from repository
        List<Comment> comments = commentsRepository.findCommentsByInventoryId(inventoryId);

        // Convert to DTOs
        return comments.stream()
                .map(comment -> CommentDTO.builder()
                        .id(comment.getId())
                        .description(comment.getDescription())
                        .author(comment.getAuthor() != null ? comment.getAuthor().getName() : "Unknown")
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public CommentDTO createComment(Integer inventoryId, CommentDTO commentDTO) {
        Optional<Inventory> inventory = inventoriesRepository.findById(inventoryId);
        if (inventory.isEmpty()) {
            throw new IllegalArgumentException("Inventory not found");
        }
        Comment comment = Comment.builder()
                .inventories(inventory.get())
                .description(commentDTO.getDescription())
                .author(inventory.get().getUser())
                .build();

        // Save and convert back to DTO
        Comment savedComment = commentsRepository.save(comment);

        return CommentDTO.builder()
                .id(savedComment.getId())
                .description(savedComment.getDescription())
                .createdAt(savedComment.getCreatedAt())
                .author(savedComment.getAuthor().getName())
                .build();
    }

    @Transactional
    public void deleteComment(Integer inventoryId, Integer commentId) {
        // Find the comment and verify it belongs to the inventory in one query
        Optional<Comment> commentOpt = commentsRepository.findByCommentIdAndInventoryId(commentId, inventoryId);

        if (commentOpt.isEmpty()) {
            throw new IllegalArgumentException("Comment not found or does not belong to the specified inventory");
        }

        // Delete the specific comment
        commentsRepository.deleteById(commentId);
    }

}
