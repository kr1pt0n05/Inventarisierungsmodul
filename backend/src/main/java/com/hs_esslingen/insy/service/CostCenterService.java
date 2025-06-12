package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.exception.BadRequest;
import com.hs_esslingen.insy.dto.CostCenterDTO;
import com.hs_esslingen.insy.model.CostCenter;
import com.hs_esslingen.insy.repository.CostCenterRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CostCenterService {

    private final CostCenterRepository costCenterRepository;

    public CostCenter resolveCostCenter(Object costCenter) {
        if (costCenter instanceof Integer costCenterId) {
            return costCenterRepository.findById(costCenterId)
                    .orElseThrow(() -> new BadRequest("Couldn't find costCenter with id: " + costCenterId));
        } else if (costCenter instanceof String costCenterName) {
            return costCenterRepository.findByName(costCenterName)
                    .orElseGet(() -> costCenterRepository.save(new CostCenter(costCenterName)));
        }
        throw new BadRequest("costCenter must be of type Integer or String");
    }
      
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