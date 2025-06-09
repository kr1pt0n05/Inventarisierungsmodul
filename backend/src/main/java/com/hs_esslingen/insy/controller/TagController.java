package com.hs_esslingen.insy.controller;

import com.hs_esslingen.insy.dto.TagDTO;
import com.hs_esslingen.insy.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<String> addTagToInventory(
            @PathVariable("id") Integer id, 
            @RequestBody List<Integer> tagIds
    ) {
        tagsService.addTagsToInventory(id, tagIds);
        return ResponseEntity.ok("Tags successfully added to inventory item");
    }

    @DeleteMapping("/inventories/{id}/tags/{tagId}")
    public ResponseEntity<String> removeTagFromInventory(
            @PathVariable("id") Integer id, 
            @PathVariable("tagId") Integer tagId
    ) {
        tagsService.removeTagFromInventory(id, tagId);
        return ResponseEntity.ok("Tag successfully removed from inventory item");
    }

}