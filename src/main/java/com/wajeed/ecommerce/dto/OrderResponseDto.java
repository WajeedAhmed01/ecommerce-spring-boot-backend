package com.wajeed.ecommerce.dto;

import com.wajeed.ecommerce.model.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderResponseDto
{
    private Long id;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private List<OrderItemResponse> orderItems;
}
