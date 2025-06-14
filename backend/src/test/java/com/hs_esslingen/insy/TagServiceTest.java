package com.hs_esslingen.insy;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.hs_esslingen.insy.dto.TagDTO;
import com.hs_esslingen.insy.exception.BadRequestException;
import com.hs_esslingen.insy.exception.NotFoundException;
import com.hs_esslingen.insy.mapper.TagMapper;
import com.hs_esslingen.insy.model.Inventory;
import com.hs_esslingen.insy.model.Tag;
import com.hs_esslingen.insy.repository.InventoryRepository;
import com.hs_esslingen.insy.repository.TagRepository;
import com.hs_esslingen.insy.service.TagService;

class TagServiceTest {

    @InjectMocks
    private TagService tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private TagMapper tagMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTagById_TagFound() {
        // Setup - Tag und TagDTO erstellen
        Tag tag = new Tag(1, "TestTag", new HashSet<>());
        TagDTO tagDTO = new TagDTO(1, "TestTag");

        // Mocking der Methodenaufrufe
        when(tagRepository.findById(1)).thenReturn(Optional.of(tag));
        when(tagMapper.toDto(tag)).thenReturn(tagDTO);

        // Methode aufrufen und Ergebnis überprüfen
        TagDTO result = tagService.getTagById(1);
        assertEquals("TestTag", result.getName());
        verify(tagRepository).findById(1);
        verify(tagMapper).toDto(tag);
    }

    @Test
    void testGetTagById_TagNotFound() {
        // Setup - Tag mit ID 1 existiert nicht
        when(tagRepository.findById(1)).thenReturn(Optional.empty());

        // Exception erwartet
        Exception exception = assertThrows(NotFoundException.class, () -> {
            tagService.getTagById(1);
        });

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void testCreateTag_Success() {
        // Setup - Neuen Tag mit ID 1 erstellen
        TagDTO dto = new TagDTO(null, "NewTag");
        Tag savedTag = new Tag(1, "NewTag", new HashSet<>());
        TagDTO savedDto = new TagDTO(1, "NewTag");

        when(tagRepository.findByName("NewTag")).thenReturn(Optional.empty());
        when(tagRepository.save(any())).thenReturn(savedTag);
        when(tagMapper.toDto(savedTag)).thenReturn(savedDto);

        // Tag erstellen und Ergebnis überprüfen
        TagDTO result = tagService.createTag(dto);
        assertEquals(1, result.getId());
        assertEquals("NewTag", result.getName());
    }

    @Test
    void testCreateTag_DuplicateName() {
        // Setup - Tag mit dem Namen "NewTag" existiert bereits
        TagDTO dto = new TagDTO(null, "NewTag");
        when(tagRepository.findByName("NewTag")).thenReturn(Optional.of(new Tag(1, "NewTag", new HashSet<>())));

        // Exception erwartet
        Exception exception = assertThrows(BadRequestException.class, () -> {
            tagService.createTag(dto);
        });

        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    void testDeleteTag_TagFound() {
        // Setup - Tag mit ID 1 existiert & soll gelöscht werden
        when(tagRepository.findById(1)).thenReturn(Optional.of(new Tag(1, "DeleteMe", new HashSet<>())));

        // Tag löschen und Überprüfung
        tagService.deleteTag(1);
        verify(tagRepository).deleteById(1);
    }

    @Test
    void testDeleteTag_TagNotFound() {
        // Setup - Tag mit ID 1 existiert nicht & soll gelöscht werden
        when(tagRepository.findById(1)).thenReturn(Optional.empty());

        // Exception erwartet
        Exception exception = assertThrows(NotFoundException.class, () -> {
            tagService.deleteTag(1);
        });

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void testGetTagsByInventoryId_InventoryFound() {
        // Setup - Inventory mit 2 Tags erstellen
        Tag tag1 = new Tag(1, "Tag1", new HashSet<>());
        Tag tag2 = new Tag(2, "Tag2", new HashSet<>());
        Set<Tag> tags = new HashSet<>(Arrays.asList(tag1, tag2));
        Inventory inv = new Inventory();
        inv.setTags(tags);

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inv));
        when(tagMapper.toDto(tag1)).thenReturn(new TagDTO(1, "Tag1"));
        when(tagMapper.toDto(tag2)).thenReturn(new TagDTO(2, "Tag2"));

        // Tags abrufen und Ergebnis überprüfen
        List<TagDTO> result = tagService.getTagsByInventoryId(1);
        assertEquals(2, result.size());
        verify(inventoryRepository).findById(1);
    }

    @Test
    void testGetTagsByInventoryId_InventoryNotFound() {
        // Setup - Inventory mit ID 1 existiert nicht
        when(inventoryRepository.findById(1)).thenReturn(Optional.empty());

        //Exception erwartet
        Exception exception = assertThrows(NotFoundException.class, () -> {
            tagService.getTagsByInventoryId(1);
        });

        assertTrue(exception.getMessage().contains("not found"));
    }
}