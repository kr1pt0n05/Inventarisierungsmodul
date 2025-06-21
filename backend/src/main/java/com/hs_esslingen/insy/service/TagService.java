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

    /**
     * Retrieves all tags from the repository and returns them as a paginated list
     * of
     * TagDTOs.
     *
     * @param pageable the pagination information
     * @return a Page of TagDTOs containing all tags
     */
    public Page<TagDTO> getAllTags(Pageable pageable) {
        Page<Tag> tagsPage = tagRepository.findAll(pageable);
        return tagsPage.map(tagMapper::toDto);
    }

    /**
     * Retrieves a tag by its ID.
     *
     * @param id the ID of the tag to be retrieved
     * @return a TagDTO containing the tag details
     * @throws NotFoundException if the tag with the given ID does not exist
     */
    public TagDTO getTagById(Integer id) {
        Optional<Tag> tag = tagRepository.findById(id);

        if (tag.isEmpty()) {
            throw new NotFoundException("Tag with id: " + id + " not found");
        }

        return tagMapper.toDto(tag.get());
    }

    /**
     * Creates a new tag with the provided details.
     *
     * @param tagDTO the DTO containing the tag details
     * @return the created TagDTO
     * @throws BadRequestException if a tag with the same name already exists
     */
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

    /**
     * Updates an existing tag with the provided details.
     *
     * @param id     the ID of the tag to be updated
     * @param tagDTO the DTO containing the updated tag details
     * @return the updated TagDTO
     * @throws NotFoundException if the tag with the given ID does not exist
     */
    @Transactional
    public void deleteTag(Integer id) {
        Optional<Tag> tag = tagRepository.findById(id);

        if (tag.isEmpty()) {
            throw new NotFoundException("Tag with id: " + id + " not found");
        }

        tagRepository.deleteById(id);
    }

    /**
     * Retrieves all tags associated with a specific inventory item.
     *
     * @param inventoryId the ID of the inventory item
     * @return a list of TagDTOs associated with the inventory item
     * @throws NotFoundException if the inventory with the given ID does not exist
     */
    public List<TagDTO> getTagsByInventoryId(Integer inventoryId) {
        Optional<Inventory> inventory = inventoryRepository.findById(inventoryId);

        if (inventory.isEmpty()) {
            throw new NotFoundException("Inventory with id: " + inventoryId + " not found");
        }

        // Get tags directly from the inventory object
        Inventory inventoryItem = inventory.get();
        List<Tag> tags = inventoryItem.getTags().stream().collect(Collectors.toList());

        return tags.stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Adds tags to an inventory item.
     *
     * @param inventoryId the ID of the inventory item
     * @param tagIds      a list of tag IDs to be added
     * @throws NotFoundException if the inventory or any of the tags do not exist
     */
    @Transactional
    public void addTagsToInventory(Integer inventoryId, List<Integer> tagIds) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new BadRequestException("Inventory with id: " + inventoryId + " not found"));

        if (tagIds == null || tagIds.isEmpty())
            return;

        Set<Tag> tags = tagIds.stream()
                .map(tagId -> tagRepository.findById(tagId)
                        .orElseThrow(() -> new NotFoundException("Tag with id: " + tagId + " not found")))
                .collect(Collectors.toSet());

        inventory.setTags(tags);

        inventoryRepository.save(inventory);
    }

    /**
     * Removes a tag from an inventory item.
     *
     * @param inventoryId the ID of the inventory item
     * @param tagId       the ID of the tag to be removed
     * @throws NotFoundException   if the inventory or the tag does not exist
     * @throws BadRequestException if the tag is not assigned to the inventory item
     */
    @Transactional
    public void removeTagFromInventory(Integer inventoryId, Integer tagId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(
                        () -> new NotFoundException("Inventory with id: " + inventoryId + " not found"));

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new NotFoundException("Tag with id: " + tagId + " not found"));

        if (!inventory.getTags().contains(tag)) {
            throw new BadRequestException(
                    "Tag with id: " + tagId + " is not assigned to inventory item with id: " + inventoryId);
        }

        inventory.getTags().remove(tag);
        inventoryRepository.save(inventory);
    }
}