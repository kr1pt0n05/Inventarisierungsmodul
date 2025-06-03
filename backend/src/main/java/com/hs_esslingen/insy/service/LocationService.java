package com.hs_esslingen.insy.service;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.hs_esslingen.insy.repository.InventoryRepository;


@RequiredArgsConstructor
@Service
public class LocationService {

    private final InventoryRepository inventoryRepository;

    // Get Locations from repository
    public Set<String> getAllLocations() {
        return inventoryRepository.findAllLocations();
    }
}