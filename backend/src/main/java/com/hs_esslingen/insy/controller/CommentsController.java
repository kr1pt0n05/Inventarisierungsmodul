package com.hs_esslingen.insy.controller;

import java.util.List;

import com.hs_esslingen.insy.dto.Comment;
import com.hs_esslingen.insy.model.Comments;
import com.hs_esslingen.insy.service.CommentsService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/inventories/{id}/comments")
public class CommentsController {
    
    private final CommentsService commentsService;
    
    CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getCommentsByInventoryId(@PathVariable("id") Integer id) {
        try {
            List<Comment> comments = commentsService.getCommentsByInventoryId(id);
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
    public ResponseEntity<Comment> createComment(@PathVariable("id") Integer inventoryId, @RequestBody Comment comment) {
        try {
            Comment createdComment = commentsService.createComment(inventoryId, comment);
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
