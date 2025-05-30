package com.hs_esslingen.insy.controller;

import java.util.List;

import com.hs_esslingen.insy.dto.Comment;
import com.hs_esslingen.insy.service.CommentsService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/inventories/{id}/comments")
public class CommentsController {
    
    private final CommentsService commentsService;

    @GetMapping
    public ResponseEntity<List<Comment>> getCommentsByInventoryId(@PathVariable("id") Integer id) {
            List<Comment> comments = commentsService.getCommentsByInventoryId(id);
            return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@PathVariable("id") Integer inventoryId, @RequestBody Comment comment) {
            Comment createdComment = commentsService.createComment(inventoryId, comment);
            return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

        @DeleteMapping("/{commentId}")
        public ResponseEntity<Void> deleteComment(@PathVariable("id") Integer inventoryId, @PathVariable("commentId") Integer commentId) {
                commentsService.deleteComment(inventoryId, commentId);
                return new ResponseEntity<>(HttpStatus.OK);
}

}
