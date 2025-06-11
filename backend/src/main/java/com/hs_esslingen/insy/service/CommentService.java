package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.hs_esslingen.insy.model.Inventory;
import com.hs_esslingen.insy.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.hs_esslingen.insy.dto.CommentDTO;
import com.hs_esslingen.insy.exception.BadRequestException;
import com.hs_esslingen.insy.exception.NotFoundException;
import com.hs_esslingen.insy.model.Comment;
import com.hs_esslingen.insy.repository.CommentRepository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final InventoryRepository inventoryRepository;


    public List<CommentDTO> getCommentsByInventoryId(Integer inventoryId) {
        Optional<Inventory> inventory = inventoryRepository.findById(inventoryId);

        if (inventory.isEmpty()) {
            throw new NotFoundException("Inventory with the id: " + inventoryId + " not found");
        }

        // Get comments from repository
        List<Comment> comments = commentRepository.findCommentsByInventoryId(inventoryId);

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
        Optional<Inventory> inventory = inventoryRepository.findById(inventoryId);
        if (inventory.isEmpty()) {
            throw new NotFoundException("Inventory with id: " + inventoryId + " not found");
        }
        Comment comment = Comment.builder()
                .inventories(inventory.get())
                .description(commentDTO.getDescription())
                .author(inventory.get().getUser())
                .build();

        // Save and convert back to DTO
        Comment savedComment = commentRepository.save(comment);

        return CommentDTO.builder()
                .id(savedComment.getId())
                .description(savedComment.getDescription())
                .createdAt(savedComment.getCreatedAt())
                .author(savedComment.getAuthor().getName())
                .build();
    }

    @Transactional
    public void deleteComment(Integer inventoryId, Integer commentId) {
        // Verify that the inventory with the given id exists
        Optional<Inventory> inventory = inventoryRepository.findById(inventoryId);

        if (inventory.isEmpty()) {
            throw new NotFoundException("Inventory with id: " + inventoryId + " not found");
        }
        // Verify that the comment belongs to the given inventoryId
        Optional<Comment> commentOpt = commentRepository.findByCommentIdAndInventoryId(commentId, inventoryId);
        if (commentOpt.isEmpty()) {
            throw new BadRequestException("Comment with id: " + commentId + " doesn't exist or doesn't belong to the inventory with id: " + inventoryId);
        }
            // Delete the specific comment
            commentRepository.deleteByCommentId(commentId);
    }
}
