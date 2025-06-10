package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.stream.Collectors;

import com.hs_esslingen.insy.dto.CompanyDTO;
import com.hs_esslingen.insy.dto.OrdererDTO;
import com.hs_esslingen.insy.model.Company;
import com.hs_esslingen.insy.model.User;
import com.hs_esslingen.insy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class OrdererService {

    private final UserRepository userRepository;

    // Get Orderers from repository
    public OrdererDTO getAllOrderers() {
        List<String> allCompanies = userRepository.findAll().stream()
                .map(User::getName)
                .sorted()
                .collect(Collectors.toList());

        return OrdererDTO.builder()
                .orderers(allCompanies)
                .build();
    }
}