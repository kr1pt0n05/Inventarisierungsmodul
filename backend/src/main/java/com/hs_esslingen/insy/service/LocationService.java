package com.hs_esslingen.insy.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.dto.LocationDTO;
import com.hs_esslingen.insy.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LocationService {

    private final InventoryRepository inventoryRepository;

    /**
     * Retrieves all unique locations from the inventory repository and returns them
     * as a LocationDTO.
     *
     * @return a LocationDTO containing a set of all unique locations
     */
    public LocationDTO getAllLocations() {
        Set<String> locations = inventoryRepository.findAllLocations();

        return LocationDTO.builder()
                .locations(locations)
                .build();
    }
}