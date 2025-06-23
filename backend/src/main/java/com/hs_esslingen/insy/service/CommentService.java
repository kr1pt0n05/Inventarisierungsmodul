package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.stream.Collectors;

import com.hs_esslingen.insy.model.User;
import com.hs_esslingen.insy.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hs_esslingen.insy.dto.CommentDTO;
import com.hs_esslingen.insy.exception.BadRequestException;
import com.hs_esslingen.insy.exception.NotFoundException;
import com.hs_esslingen.insy.model.Comment;
import com.hs_esslingen.insy.model.Inventory;
import com.hs_esslingen.insy.repository.CommentRepository;
import com.hs_esslingen.insy.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {

        private final CommentRepository commentRepository;
        private final InventoryRepository inventoryRepository;
        private final UserRepository userRepository;

        /**
         * Retrieves all comments for a specific inventory item by its ID.
         *
         * @param inventoryId the ID of the inventory item
         * @return a list of CommentDTOs containing the comments for the specified
         *         inventory
         * @throws NotFoundException if the inventory with the given ID does not exist
         */
        public List<CommentDTO> getCommentsByInventoryId(Integer inventoryId) {
                // Verify that the provided Inventory exists
                inventoryRepository.findById(inventoryId)
                                .orElseThrow(() -> new NotFoundException(
                                                "Inventory with id: " + inventoryId + " not found"));

                // Get comments from repository
                List<Comment> comments = commentRepository.findCommentsByInventoryId(inventoryId);

                // Convert to DTO
                return comments.stream()
                                .map(comment -> CommentDTO.builder()
                                                .id(comment.getId())
                                                .description(comment.getDescription())
                                                .author(comment.getAuthor() != null ? comment.getAuthor().getName()
                                                                : "Unknown")
                                                .createdAt(comment.getCreatedAt())
                                                .build())
                                .collect(Collectors.toList());
        }

        /**
         * Creates a new comment for a specific inventory item.
         *
         * @param inventoryId the ID of the inventory item
         * @param commentDTO  the DTO containing the comment details
         * @return the created CommentDTO
         * @throws NotFoundException if the inventory with the given ID does not exist
         */
        public CommentDTO createComment(Integer inventoryId, CommentDTO commentDTO) {
                Inventory inventory = inventoryRepository.findById(inventoryId)
                        .orElseThrow(() -> new NotFoundException("Inventory with id: " + inventoryId + " not found"));

                User author = userRepository.findByName(commentDTO.getAuthor())
                        .orElseGet(() -> userRepository.save(new User(commentDTO.getAuthor())));

                Comment comment = Comment.builder()
                        .inventories(inventory)
                        .description(commentDTO.getDescription())
                        .author(author)
                        .build();

                Comment savedComment = commentRepository.save(comment);

                return CommentDTO.builder()
                        .id(savedComment.getId())
                        .description(savedComment.getDescription())
                        .createdAt(savedComment.getCreatedAt())
                        .author(author.getName())
                        .build();
        }

        /**
         * Deletes a specific comment from an inventory item.
         *
         * @param inventoryId the ID of the inventory item
         * @param commentId   the ID of the comment to delete
         * @throws NotFoundException   if the inventory with the given ID does not exist
         * @throws BadRequestException if the comment does not belong to the specified
         *                             inventory or does not exist
         */
        @Transactional
        public void deleteComment(Integer inventoryId, Integer commentId) {
                // Verify that the inventory with the given id exists
                inventoryRepository.findById(inventoryId)
                                .orElseThrow(() -> new NotFoundException(
                                                "Inventory with id: " + inventoryId + " not found"));

                // Verify that the comment belongs to the given inventoryId
                commentRepository.findByCommentIdAndInventoryId(commentId, inventoryId)
                                .orElseThrow(() -> new BadRequestException("Comment with id: " + commentId
                                                + " doesn't exist or doesn't belong to the inventory with id: "
                                                + inventoryId));

                // Delete the specific comment
                commentRepository.deleteByCommentId(commentId);
        }
}
