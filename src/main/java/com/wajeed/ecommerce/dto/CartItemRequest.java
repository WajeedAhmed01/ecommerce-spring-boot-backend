package com.wajeed.ecommerce.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class CartItemRequest {

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}