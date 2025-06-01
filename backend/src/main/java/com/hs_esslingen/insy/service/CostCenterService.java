package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.hs_esslingen.insy.model.CostCenters;
import com.hs_esslingen.insy.repository.CostCentersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CostCenterService {

    private final CostCentersRepository costCentersRepository;

        // Get CostCenters from repository
        public List<String> getAllCostCenter() {
            return costCentersRepository.findAll().stream()
                    .map(CostCenters::getDescription)
                    .collect(Collectors.toList());
        }
    }