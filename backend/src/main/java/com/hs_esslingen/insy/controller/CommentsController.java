package com.hs_esslingen.insy.controller;

import java.util.List;

import com.hs_esslingen.insy.dto.CommentDTO;
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
    public ResponseEntity<List<CommentDTO>> getCommentsByInventoryId(@PathVariable("id") Integer id) {
        List<CommentDTO> comments = commentsService.getCommentsByInventoryId(id);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@PathVariable("id") Integer inventoryId, @RequestBody CommentDTO comment) {
        CommentDTO createdComment = commentsService.createComment(inventoryId, comment);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable("id") Integer inventoryId, @PathVariable("commentId") Integer commentId) {
        commentsService.deleteComment(inventoryId, commentId);
    }
}
