package com.hs_esslingen.insy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hs_esslingen.insy.service.OrdererService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/inventories/orderers")
public class OrdererController {

    private final OrdererService ordererService;

    @GetMapping
    public ResponseEntity<List<String>> getAllOrderers() {
        List<String> orderers = ordererService.getAllCompanies();
        return new ResponseEntity<>(orderers, HttpStatus.OK);
    }
}