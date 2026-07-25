package com.wajeed.ecommerce.controller;

import com.wajeed.ecommerce.dto.OrderResponseDto;
import com.wajeed.ecommerce.security.MyUserDetails;
import com.wajeed.ecommerce.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<OrderResponseDto> placeOrder( @RequestHeader("Idempotency-Key" )
                                                            String idempotencyKey,
            Authentication authentication) {
        OrderResponseDto response = orderService.placeAnOrder(authentication , idempotencyKey);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getUserOrderHistory(
            Authentication authentication) {
        List<OrderResponseDto> history = orderService.getUserOrderHistory(authentication);
        return ResponseEntity.ok(history);
    }
}