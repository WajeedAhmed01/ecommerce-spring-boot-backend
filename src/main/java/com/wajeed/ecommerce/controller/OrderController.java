package com.wajeed.ecommerce.controller;

import com.wajeed.ecommerce.dto.OrderResponseDto;
import com.wajeed.ecommerce.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> placeOrder(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            Authentication authentication) {

        OrderResponseDto response =
                orderService.placeAnOrder(authentication, idempotencyKey);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getUserOrderHistory(
            Authentication authentication) {

        List<OrderResponseDto> history =
                orderService.getUserOrderHistory(authentication);

        return ResponseEntity.ok(history);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> viewOrderById(
            @PathVariable Long orderId,
            Authentication authentication) {

        OrderResponseDto order =
                orderService.viewOrderById(authentication, orderId);

        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponseDto> cancelOrder(
            @PathVariable Long orderId,
            Authentication authentication) {

        OrderResponseDto response =
                orderService.cancelOrder(authentication, orderId);

        return ResponseEntity.ok(response);
    }
}