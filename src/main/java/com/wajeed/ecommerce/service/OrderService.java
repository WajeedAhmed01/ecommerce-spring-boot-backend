package com.wajeed.ecommerce.service;

import com.wajeed.ecommerce.dto.OrderResponseDto;
import com.wajeed.ecommerce.security.MyUserDetails;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface OrderService
{
    public List<OrderResponseDto> getUserOrderHistory(Authentication authentication);
    public OrderResponseDto placeAnOrder(Authentication authentication ,String idempotencyKey);
    public OrderResponseDto viewOrderById(Authentication authentication , Long orderId);
    public OrderResponseDto cancelOrder(Authentication authentication, Long orderId);
}
