package com.hs_esslingen.insy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hs_esslingen.insy.dto.ExtensionsCreateDTO;
import com.hs_esslingen.insy.dto.ExtensionsResponseDTO;
import com.hs_esslingen.insy.service.ExtensionsService;

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
public class ExtensionsController {
    
    private final ExtensionsService extensionsService;
    
    ExtensionsController(ExtensionsService extensionsService) {
        this.extensionsService = extensionsService;
    }
    
    @GetMapping
    public ResponseEntity<List<ExtensionsResponseDTO>> getAllExtensions(@PathVariable Integer id) {
        List<ExtensionsResponseDTO> extensions = extensionsService.getAllExtensions(id);
        return ResponseEntity.ok(extensions);
    }

    @PostMapping
    public ResponseEntity<ExtensionsResponseDTO> createExtension(@PathVariable Integer id, @RequestBody ExtensionsCreateDTO extensionData) {
        
        ExtensionsResponseDTO response = extensionsService.addExtension(id, extensionData);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/{componentId}")
    public ResponseEntity<ExtensionsResponseDTO> getExtensionById(@PathVariable Integer id, @PathVariable Integer componentId) {
        ExtensionsResponseDTO extension = extensionsService.getExtensionById(id, componentId);
        return ResponseEntity.ok(extension);
    }
    @PatchMapping("/{componentId}")
    public ResponseEntity<ExtensionsResponseDTO> updateExtension(@PathVariable Integer id, @PathVariable Integer componentId, @RequestBody ExtensionsCreateDTO extensionData) {
        ExtensionsResponseDTO updatedExtension = extensionsService.updateExtension(id, componentId, extensionData);
        return ResponseEntity.ok(updatedExtension);
    }
    @DeleteMapping("/{componentId}")
    public ResponseEntity<Void> deleteExtension(@PathVariable Integer id, @PathVariable Integer componentId) {
        extensionsService.deleteExtension(id, componentId);
        return ResponseEntity.noContent().build();
    }
    
    
}
