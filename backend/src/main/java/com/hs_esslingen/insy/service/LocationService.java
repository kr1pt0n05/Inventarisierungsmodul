package com.hs_esslingen.insy.service;

import java.util.Set;

import com.hs_esslingen.insy.dto.LocationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.hs_esslingen.insy.repository.InventoryRepository;


@RequiredArgsConstructor
@Service
public class LocationService {

    private final InventoryRepository inventoryRepository;

    public LocationDTO getAllLocations() {
        Set<String> locations = inventoryRepository.findAllLocations();

        return LocationDTO.builder()
                .locations(locations)
                .build();
    }
}