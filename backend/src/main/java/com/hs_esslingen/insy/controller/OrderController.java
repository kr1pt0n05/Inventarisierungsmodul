package com.hs_esslingen.insy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hs_esslingen.insy.dto.OrderCreateDTO;
import com.hs_esslingen.insy.dto.OrderResponseDTO;
import com.hs_esslingen.insy.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * Retrieves all open orders.
     *
     * @return a ResponseEntity containing a list of OrderResponseDTOs
     */
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getOpenOrders() {
        return orderService.getAllOpenOrders();
    }

    /**
     * Creates a new order. Called by BeSy to transmit a new order.
     *
     * @param orderRequest an array of MockBesyOrderDTO containing the order details
     * @return a ResponseEntity indicating the result of the creation
     */
    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody OrderCreateDTO[] orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    /**
     * Deletes an order by its ID.
     *
     * @param id the ID of the order to delete
     * @return a ResponseEntity indicating the result of the deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> markOrderAsProcessed(@PathVariable Integer id) {
        return orderService.markOrderAsProcessed(id);
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id the ID of the order to retrieve
     * @return a ResponseEntity containing the OrderResponseDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Integer id) {
        return orderService.getOrderById(id);
    }

    /**
     * Retrieves all orders.
     *
     * @return a ResponseEntity containing a list of OrderResponseDTOs
     */
    @GetMapping("/all")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return orderService.getAllOrders();
    }
}
