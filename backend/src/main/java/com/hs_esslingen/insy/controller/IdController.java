package com.hs_esslingen.insy.controller;

import com.hs_esslingen.insy.dto.IdDTO;
import com.hs_esslingen.insy.service.IdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/inventories")
public class IdController {
    private final IdService idService;

    @GetMapping("/maxAndMinId")
    public ResponseEntity<IdDTO> getMaxAndMinId() {
        IdDTO id = idService.getMaxAndMinId();
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
