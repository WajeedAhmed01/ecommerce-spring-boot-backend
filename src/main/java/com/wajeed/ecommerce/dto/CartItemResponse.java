package com.wajeed.ecommerce.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Setter
@Getter
public class CartItemResponse
{
    Long cartItemId;
    Long ProductId;
    String ProductName;
    BigDecimal price;
    int Quantity;
    BigDecimal Subtotal;
}
