package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hs_esslingen.insy.dto.TagDTO;
import com.hs_esslingen.insy.exception.BadRequestException;
import com.hs_esslingen.insy.exception.NotFoundException;
import com.hs_esslingen.insy.mapper.TagMapper;
import com.hs_esslingen.insy.model.Inventory;
import com.hs_esslingen.insy.model.Tag;
import com.hs_esslingen.insy.repository.InventoryRepository;
import com.hs_esslingen.insy.repository.TagRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;
    private final InventoryRepository inventoryRepository;
    private final TagMapper tagMapper;

    public Page<TagDTO> getAllTags(Pageable pageable) {
        Page<Tag> tagsPage = tagRepository.findAll(pageable);
        return tagsPage.map(tagMapper::toDto);
    }

    public TagDTO getTagById(Integer id) {
        Optional<Tag> tag = tagRepository.findById(id);

        if (tag.isEmpty()) {
            throw new NotFoundException("Tag with the id: " + id + " not found");
        }

        return tagMapper.toDto(tag.get());
    }

    public TagDTO createTag(TagDTO tagDTO) {
        // Check if tag with same name already exists
        Optional<Tag> existingTag = tagRepository.findByName(tagDTO.getName());
        if (existingTag.isPresent()) {
            throw new BadRequestException("Tag with the name: " + tagDTO.getName() + " already exists");
        }

        Tag tag = Tag.builder()
                .name(tagDTO.getName())
                .build();

        Tag savedTag = tagRepository.save(tag);
        return tagMapper.toDto(savedTag);
    }

    @Transactional
    public void deleteTag(Integer id) {
        Optional<Tag> tag = tagRepository.findById(id);

        if (tag.isEmpty()) {
            throw new NotFoundException("Tag with the id: " + id + " not found");
        }

        tagRepository.deleteById(id);
    }

    public List<TagDTO> getTagsByInventoryId(Integer inventoryId) {
        Optional<Inventory> inventory = inventoryRepository.findById(inventoryId);

        if (inventory.isEmpty()) {
            throw new NotFoundException("Inventory with the id: " + inventoryId + " not found");
        }

        // Get tags directly from the inventory object
        Inventory inventoryItem = inventory.get();
        List<Tag> tags = inventoryItem.getTags().stream().collect(Collectors.toList());

        return tags.stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toList());
    }

    // Adds tags to an inventory item. If the inventory item does not exist, it
    // throws a BadRequest exception.
    @Transactional
    public void addTagsToInventory(Integer inventoryId, List<Integer> tagIds) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new BadRequestException("Inventory with the id: " + inventoryId + " not found"));

        if (tagIds == null || tagIds.isEmpty())
            return;

        Set<Tag> tags = tagIds.stream()
                .map(tagId -> tagRepository.findById(tagId)
                        .orElseThrow(() -> new NotFoundException("Tag not found: " + tagId)))
                .collect(Collectors.toSet());

        inventory.setTags(tags);

        inventoryRepository.save(inventory);
    }

    @Transactional
    public void removeTagFromInventory(Integer inventoryId, Integer tagId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(
                        () -> new NotFoundException("Inventory with the id: " + inventoryId + " not found"));

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new NotFoundException("Tag with the id: " + tagId + " not found"));

        if (!inventory.getTags().contains(tag)) {
            throw new BadRequestException(
                    "Tag with the id: " + tagId + " is not assigned to inventory item " + inventoryId);
        }

        inventory.getTags().remove(tag);
        inventoryRepository.save(inventory);
    }
}