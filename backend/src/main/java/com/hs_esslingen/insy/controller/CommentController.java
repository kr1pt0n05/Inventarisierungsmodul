package com.hs_esslingen.insy.controller;

import java.util.List;

import com.hs_esslingen.insy.dto.CommentDTO;
import com.hs_esslingen.insy.service.CommentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/inventories/{id}/comments")
public class CommentController {
    
    private final CommentService commentsService;
    
    CommentController(CommentService commentsService) {
        this.commentsService = commentsService;
    }

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getCommentsByInventoryId(@PathVariable("id") Integer id) {
        try {
            List<CommentDTO> comments = commentsService.getCommentsByInventoryId(id);
            if (comments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return ResponseEntity.ok(comments);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@PathVariable("id") Integer inventoryId, @RequestBody CommentDTO comment) {
        try {
            CommentDTO createdComment = commentsService.createComment(inventoryId, comment);
            return new ResponseEntity<>(createdComment, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

        @DeleteMapping("/{commentId}")
        public ResponseEntity<Void> deleteComment(@PathVariable("id") Integer inventoryId, @PathVariable("commentId") Integer commentId) {
            try {
                commentsService.deleteComment(inventoryId, commentId);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
}

}
