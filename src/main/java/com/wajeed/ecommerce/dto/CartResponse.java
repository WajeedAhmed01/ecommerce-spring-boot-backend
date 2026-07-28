package com.wajeed.ecommerce.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CartResponse
{
    Long cartId;
    Long userID;
    BigDecimal totalPrice;

    List<CartItemResponse> cartItemResponse;
}
