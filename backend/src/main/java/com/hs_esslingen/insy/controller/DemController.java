package com.hs_esslingen.insy.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class DemController {

    @GetMapping
    public String hello1() {
        return "Hello World!";
    }

    @GetMapping("/login")
    public String hello2() {
        return "Hello World from Keycloak!";
    }
}
