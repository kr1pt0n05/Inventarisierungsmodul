package com.hs_esslingen.insy.controller;


import com.hs_esslingen.insy.dto.SerialNumberDTO;
import com.hs_esslingen.insy.service.SerialNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/serialNumbers")
public class SerialNumberController {

    private final SerialNumberService serialNumberService;

    @GetMapping
    public ResponseEntity<SerialNumberDTO> getAllSerialNumbers() {
        SerialNumberDTO serialNumbers = serialNumberService.getAllSerialNumbers();
        return new ResponseEntity<>(serialNumbers, HttpStatus.OK);
    }
}