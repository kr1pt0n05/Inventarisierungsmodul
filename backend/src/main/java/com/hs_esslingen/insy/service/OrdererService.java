package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.stream.Collectors;

import com.hs_esslingen.insy.exception.NotFoundException;
import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.dto.OrdererDTO;
import com.hs_esslingen.insy.exception.BadRequestException;
import com.hs_esslingen.insy.model.User;
import com.hs_esslingen.insy.repository.UserRepository;

import lombok.RequiredArgsConstructor;

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

    public User resolveUser(Object orderer) {
        if (orderer instanceof Integer userId) {
            return userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("Orderer with id: " + userId + " not found"));
        } else if (orderer instanceof String userName) {
            return userRepository.findByName(userName)
                    .orElseGet(() -> userRepository.save(new User(userName)));
        }
        throw new BadRequestException("orderer must be of type Integer or String.");
    }
}