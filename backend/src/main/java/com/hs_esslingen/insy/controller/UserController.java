package com.hs_esslingen.insy.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hs_esslingen.insy.dto.UserDTO;
import com.hs_esslingen.insy.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orderers")
public class UserController {

    private final UserService ordererService;

    @GetMapping
    public ResponseEntity<UserDTO> getAllOrderers() {
        UserDTO orderers = ordererService.getAllOrderers();
        return new ResponseEntity<>(orderers, HttpStatus.OK);
    }
}