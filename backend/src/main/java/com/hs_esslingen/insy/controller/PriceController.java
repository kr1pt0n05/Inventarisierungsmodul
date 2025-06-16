package com.hs_esslingen.insy.controller;


import com.hs_esslingen.insy.dto.PriceDTO;
import com.hs_esslingen.insy.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/inventories")
public class PriceController {
    private final PriceService priceService;

    @GetMapping("/maxAndMinPrice")
    public ResponseEntity<PriceDTO> getMaxAndMinPrice() {
        PriceDTO price = priceService.getMaxAndMinPrice();
        return new ResponseEntity<>(price, HttpStatus.OK);
    }


}
