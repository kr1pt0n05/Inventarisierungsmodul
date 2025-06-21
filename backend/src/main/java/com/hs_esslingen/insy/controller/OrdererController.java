package com.hs_esslingen.insy.controller;


import com.hs_esslingen.insy.dto.OrdererDTO;
import com.hs_esslingen.insy.service.OrdererService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orderers")
public class OrdererController {

    private final OrdererService ordererService;

    @GetMapping
    public ResponseEntity<OrdererDTO> getAllOrderers() {
        OrdererDTO orderers = ordererService.getAllOrderers();
        return new ResponseEntity<>(orderers, HttpStatus.OK);
    }
}