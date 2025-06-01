package com.hs_esslingen.insy.controller;

import java.util.List;
import java.util.Set;


import com.hs_esslingen.insy.service.OrdererService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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