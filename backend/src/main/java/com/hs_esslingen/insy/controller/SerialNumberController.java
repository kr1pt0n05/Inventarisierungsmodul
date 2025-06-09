package com.hs_esslingen.insy.controller;

import java.util.Set;
import com.hs_esslingen.insy.service.SerialNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/inventories/serialNumbers")
public class SerialNumberController {

    private final SerialNumberService serialNumberService;

    @GetMapping
    public ResponseEntity<Set<String>> getAllSerialNumbers() {
        Set<String> serialNumbers = serialNumberService.getAllSerialNumbers();
        return new ResponseEntity<>(serialNumbers, HttpStatus.OK);
    }
}