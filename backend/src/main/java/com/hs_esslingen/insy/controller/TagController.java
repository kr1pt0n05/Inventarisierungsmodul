package com.hs_esslingen.insy.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hs_esslingen.insy.dto.TagDTO;
import com.hs_esslingen.insy.service.TagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TagController {

    private final TagService tagsService;

    @GetMapping("/tags")
    public ResponseEntity<Page<TagDTO>> getAllTags(@PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(tagsService.getAllTags(pageable));
    }

    @GetMapping("/tags/{id}")
    public ResponseEntity<TagDTO> getTagById(@PathVariable Integer id) {
        return ResponseEntity.ok(tagsService.getTagById(id));
    }

    @PostMapping("/tags")
    public ResponseEntity<TagDTO> createTag(@RequestBody TagDTO tagDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tagsService.createTag(tagDTO));
    }

    @DeleteMapping("/tags/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Integer id) {
        tagsService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/inventories/{id}/tags")
    public ResponseEntity<List<TagDTO>> getTagsByInventoryId(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(tagsService.getTagsByInventoryId(id));
    }

    @PostMapping("/inventories/{id}/tags")
    public ResponseEntity<List<TagDTO>> addTagToInventory(
            @PathVariable("id") Integer id,
            @RequestBody List<Integer> tagIds) {
        tagsService.addTagsToInventory(id, tagIds);
        return ResponseEntity.ok(tagsService.getTagsByInventoryId(id));
    }

    @DeleteMapping("/inventories/{id}/tags/{tagId}")
    public ResponseEntity<String> removeTagFromInventory(
            @PathVariable("id") Integer id,
            @PathVariable("tagId") Integer tagId) {
        tagsService.removeTagFromInventory(id, tagId);
        return ResponseEntity.noContent().build();
    }

}
