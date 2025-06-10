package com.hs_esslingen.insy.controller;

import java.util.List;
import java.util.Set;

import com.hs_esslingen.insy.dto.CostCenterDTO;
import com.hs_esslingen.insy.service.CostCenterService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/inventories/costCenters")
public class CostCenterController {

    private final CostCenterService costCenterService;

    @GetMapping
    public ResponseEntity<List<CostCenterDTO>> getAllCostCenter() {
        List<CostCenterDTO> costCenters = costCenterService.getAllCostCenter();
        return new ResponseEntity<>(costCenters, HttpStatus.OK);
    }
}