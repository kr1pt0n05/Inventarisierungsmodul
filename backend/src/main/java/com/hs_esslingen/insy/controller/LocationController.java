package com.hs_esslingen.insy.controller;

import java.util.List;
import java.util.Set;


import com.hs_esslingen.insy.dto.LocationDTO;
import com.hs_esslingen.insy.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/inventories/locations")
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<LocationDTO> getAllLocations() {
        LocationDTO locations = locationService.getAllLocations();
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }
}