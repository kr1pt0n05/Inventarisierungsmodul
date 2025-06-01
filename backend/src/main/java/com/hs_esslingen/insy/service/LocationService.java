package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.Set;

import com.hs_esslingen.insy.repository.InventoriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class LocationService {

    private final InventoriesRepository inventoriesRepository;

    // Get Locations from repository
    public Set<String> getAllLocations() {
        return inventoriesRepository.findAllLocations();
    }
}