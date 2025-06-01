package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.hs_esslingen.insy.model.Users;
import com.hs_esslingen.insy.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class OrdererService {

    private final UsersRepository usersRepository;

    // Get Orderers from repository
    public List<String> getAllCompanies() {
        return usersRepository.findAll().stream()
                .map(Users::getName)
                .collect(Collectors.toList());
    }
}