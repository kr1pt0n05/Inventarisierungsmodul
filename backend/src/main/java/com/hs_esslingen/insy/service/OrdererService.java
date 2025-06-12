package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.exception.BadRequest;
import com.hs_esslingen.insy.dto.OrdererDTO;
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

    public User resolveUser(Object orderer) {
        if (orderer instanceof Integer userId) {
            return userRepository.findById(userId)
                    .orElseThrow(() -> new BadRequest("Couldn't find orderer with id: " + userId));
        } else if (orderer instanceof String userName) {
            return userRepository.findByName(userName)
                    .orElseGet(() -> userRepository.save(new User(userName)));
        }
        throw new BadRequest("orderer must be of type Integer or String.");
    }
}