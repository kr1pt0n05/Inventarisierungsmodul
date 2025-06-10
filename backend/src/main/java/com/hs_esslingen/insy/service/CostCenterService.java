package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.stream.Collectors;

import com.hs_esslingen.insy.dto.CostCenterDTO;
import com.hs_esslingen.insy.model.CostCenter;
import com.hs_esslingen.insy.repository.CostCenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CostCenterService {

    private final CostCenterRepository costCenterRepository;

    // Get CostCenters from repository
    public CostCenterDTO getAllCostCenter() {
        List<String> allDescriptions = costCenterRepository.findAll().stream()
                .map(CostCenter::getDescription)
                .sorted()
                .collect(Collectors.toList());

        return CostCenterDTO.builder()
                .costCenters(allDescriptions)
                .build();

    }
}