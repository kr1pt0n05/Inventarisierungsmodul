package com.hs_esslingen.insy.service;

import java.util.List;
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
        //Verify that the provided Inventory exists
        inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new NotFoundException("Inventory with id: " + inventoryId + " not found"));;

        // Get comments from repository
        List<Comment> comments = commentRepository.findCommentsByInventoryId(inventoryId);

        // Convert to DTO
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
        //Verify that the provided Inventory exists
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new NotFoundException("Inventory with id: " + inventoryId + " not found"));

        Comment comment = Comment.builder()
                .inventories(inventory)
                .description(commentDTO.getDescription())
                .author(inventory.getUser())
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
            inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new NotFoundException("Inventory with id: " + inventoryId + " not found"));

            // Verify that the comment belongs to the given inventoryId
            commentRepository.findByCommentIdAndInventoryId(commentId, inventoryId)
                .orElseThrow(() -> new BadRequestException("Comment with id: " + commentId + " doesn't exist or doesn't belong to the inventory with id: " + inventoryId));

            // Delete the specific comment
            commentRepository.deleteByCommentId(commentId);
    }
}
