package com.hs_esslingen.insy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.hs_esslingen.insy.dto.CommentDTO;
import com.hs_esslingen.insy.exception.BadRequestException;
import com.hs_esslingen.insy.exception.NotFoundException;
import com.hs_esslingen.insy.model.Comment;
import com.hs_esslingen.insy.model.Inventory;
import com.hs_esslingen.insy.model.User;
import com.hs_esslingen.insy.repository.CommentRepository;
import com.hs_esslingen.insy.repository.InventoryRepository;
import com.hs_esslingen.insy.service.CommentService;

class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testGetCommentsByInventoryId_success() {
        // Setup - User und Inventory initialisieren
        User user = User.builder().name("Max Mustermann").build();
        Inventory inventory = new Inventory();
        inventory.setId(1);

        // Comment mit gemocktem Author erstellen
        Comment comment = mock(Comment.class);
        when(comment.getId()).thenReturn(42);
        when(comment.getDescription()).thenReturn("Test comment");
        when(comment.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(comment.getAuthor()).thenReturn(user); // Wichtig: Author mocken
        when(comment.getInventories()).thenReturn(inventory);

        // Repository-Mocks
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inventory));
        when(commentRepository.findCommentsByInventoryId(1)).thenReturn(List.of(comment));

        // Test ausführen
        List<CommentDTO> result = commentService.getCommentsByInventoryId(1);
        
        // Assertions
        assertEquals(1, result.size());
        assertEquals("Test comment", result.get(0).getDescription());
        assertEquals("Max Mustermann", result.get(0).getAuthor());
    }

    @Test
    void testGetCommentsByInventoryId_inventoryNotFound() {
        // Setup - Inventory mit ID 999 nicht gefunden
        when(inventoryRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> commentService.getCommentsByInventoryId(999));
    }

    @Test
    void testCreateComment_success() {
        // Setup - User und Inventory initialisieren
        User user = User.builder().name("Lisa Testerin").build();
        Inventory inventory = new Inventory();
        inventory.setId(1);
        inventory.setUser(user); // User im Inventory setzen

        //  CommentDTO initialisieren
        CommentDTO commentDTO = CommentDTO.builder()
                .description("Neuer Kommentar")
                .build();

        // Gemockter gespeicherter Comment
        Comment savedComment = mock(Comment.class);
        when(savedComment.getId()).thenReturn(42);
        when(savedComment.getDescription()).thenReturn("Neuer Kommentar");
        when(savedComment.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(savedComment.getAuthor()).thenReturn(user); // Wichtig: Author mocken
        when(savedComment.getInventories()).thenReturn(inventory);

        // Repository-Mocks
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inventory));
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        // Test ausführen
        CommentDTO result = commentService.createComment(1, commentDTO);

        // Assertions
        assertEquals("Neuer Kommentar", result.getDescription());
        assertEquals("Lisa Testerin", result.getAuthor());
        assertNotNull(result.getCreatedAt());
        assertEquals(42, result.getId());
        
        // Verify dass das Repository aufgerufen wurde
        verify(commentRepository, times(1)).save(any(Comment.class));
        verify(inventoryRepository, times(1)).findById(1);
    }

    @Test
    void testCreateComment_inventoryNotFound() {
        // Setup - Inventory mit ID 999 nicht gefunden
        when(inventoryRepository.findById(999)).thenReturn(Optional.empty());

        CommentDTO commentDTO = CommentDTO.builder().description("fail").build();
        assertThrows(NotFoundException.class, () -> commentService.createComment(999, commentDTO));
    }

    @Test
    void testDeleteComment_success() {
        // Setup - Inventory und Comment initialisieren
        Inventory inventory = new Inventory();
        inventory.setId(1);

        Comment comment = new Comment();
        comment.setId(2);
        comment.setInventories(inventory);

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inventory));
        when(commentRepository.findByCommentIdAndInventoryId(2, 1)).thenReturn(Optional.of(comment));

        commentService.deleteComment(1, 2);

        verify(commentRepository, times(1)).deleteByCommentId(2);
    }

    @Test
    void testDeleteComment_inventoryNotFound() {
        // Setup - Inventory mit ID 99 nicht gefunden
        when(inventoryRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> commentService.deleteComment(99, 1));
    }

    @Test
    void testDeleteComment_commentNotFound() {
        // Setup - Inventory mit ID 1 gefunden, aber Comment mit ID 99 nicht
        Inventory inventory = new Inventory();
        inventory.setId(1);

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inventory));
        when(commentRepository.findByCommentIdAndInventoryId(99, 1)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> commentService.deleteComment(1, 99));
    }
}