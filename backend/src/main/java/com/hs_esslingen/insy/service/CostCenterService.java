package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.stream.Collectors;
import com.hs_esslingen.insy.model.CostCenter;
import com.hs_esslingen.insy.repository.CostCenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CostCenterService {

    private final CostCenterRepository costCenterRepository;

        // Get CostCenters from repository
        public List<String> getAllCostCenter() {
            return costCenterRepository.findAll().stream()
                    .map(CostCenter::getDescription)
                    .collect(Collectors.toList());
        }
    }