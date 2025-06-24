package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.dto.UserDTO;
import com.hs_esslingen.insy.exception.BadRequestException;
import com.hs_esslingen.insy.exception.NotFoundException;
import com.hs_esslingen.insy.model.User;
import com.hs_esslingen.insy.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    /*
     * Retrieves all orderers (users) from the repository and returns them as a DTO.
     * The orderers are sorted alphabetically by their names.
     *
     * @return an OrdererDTO containing a list of all orderers
     */
    public UserDTO getAllOrderers() {
        List<String> allCompanies = userRepository.findAll().stream()
                .map(User::getName)
                .sorted()
                .collect(Collectors.toList());

        return UserDTO.builder()
                .orderers(allCompanies)
                .build();
    }

    /**
     * Resolves a user based on the provided orderer identifier.
     * The orderer can be either an Integer (user ID) or a String (user name).
     *
     * @param orderer the identifier of the orderer
     * @return the resolved User object
     * @throws NotFoundException   if the user is not found
     * @throws BadRequestException if the orderer is neither an Integer nor a String
     */
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