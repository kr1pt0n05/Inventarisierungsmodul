package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.dto.CostCenterDTO;
import com.hs_esslingen.insy.exception.BadRequestException;
import com.hs_esslingen.insy.exception.NotFoundException;
import com.hs_esslingen.insy.model.CostCenter;
import com.hs_esslingen.insy.repository.CostCenterRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CostCenterService {

    private final CostCenterRepository costCenterRepository;

    /**
     * Resolves a cost center based on the provided identifier.
     * If the identifier is an Integer, it looks up the cost center by ID.
     * If it's a String, it looks up the cost center by name, creating a new one if
     * not found.
     *
     * @param costCenter the identifier of the cost center (Integer ID or String
     *                   name)
     * @return the resolved CostCenter object
     * @throws NotFoundException   if the cost center with the given ID is not found
     * @throws BadRequestException if the costCenter parameter is neither Integer
     *                             nor String
     */
    public CostCenter resolveCostCenter(Object costCenter) {
        if (costCenter instanceof Integer costCenterId) {
            return costCenterRepository.findById(costCenterId)
                    .orElseThrow(() -> new NotFoundException("costCenter with id: " + costCenterId + " not found"));
        } else if (costCenter instanceof String costCenterName) {
            return costCenterRepository.findByName(costCenterName)
                    .orElseGet(() -> costCenterRepository.save(new CostCenter(costCenterName)));
        }
        throw new BadRequestException("costCenter must be of type Integer or String");
    }

    /**
     * Retrieves all cost centers from the repository and returns them as a
     * CostCenterDTO.
     * The cost centers are sorted alphabetically by their descriptions.
     *
     * @return a CostCenterDTO containing a list of all cost centers
     */
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