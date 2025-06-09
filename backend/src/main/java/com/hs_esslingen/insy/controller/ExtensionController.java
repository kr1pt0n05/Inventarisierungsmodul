package com.hs_esslingen.insy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hs_esslingen.insy.dto.ExtensionCreateDTO;
import com.hs_esslingen.insy.dto.ExtensionResponseDTO;
import com.hs_esslingen.insy.service.ExtensionService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/inventories/{id}/components")
public class ExtensionController {
    
    private final ExtensionService extensionsService;
    
    ExtensionController(ExtensionService extensionsService) {
        this.extensionsService = extensionsService;
    }
    
    @GetMapping
    public ResponseEntity<List<ExtensionResponseDTO>> getAllExtensions(@PathVariable Integer id) {
        List<ExtensionResponseDTO> extensions = extensionsService.getAllExtensions(id);
        return ResponseEntity.ok(extensions);
    }

    @PostMapping
    public ResponseEntity<ExtensionResponseDTO> createExtension(@PathVariable Integer id, @RequestBody ExtensionCreateDTO extensionData) {
        
        ExtensionResponseDTO response = extensionsService.addExtension(id, extensionData);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/{componentId}")
    public ResponseEntity<ExtensionResponseDTO> getExtensionById(@PathVariable Integer id, @PathVariable Integer componentId) {
        ExtensionResponseDTO extension = extensionsService.getExtensionById(id, componentId);
        return ResponseEntity.ok(extension);
    }
    @PatchMapping("/{componentId}")
    public ResponseEntity<ExtensionResponseDTO> updateExtension(@PathVariable Integer id, @PathVariable Integer componentId, @RequestBody ExtensionCreateDTO extensionData) {
        ExtensionResponseDTO updatedExtension = extensionsService.updateExtension(id, componentId, extensionData);
        return ResponseEntity.ok(updatedExtension);
    }
    @DeleteMapping("/{componentId}")
    public ResponseEntity<Void> deleteExtension(@PathVariable Integer id, @PathVariable Integer componentId) {
        extensionsService.deleteExtension(id, componentId);
        return ResponseEntity.noContent().build();
    }
    
    
}
