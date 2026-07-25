package com.wajeed.ecommerce.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductCreateRequest
{
    @Column(nullable = false, unique = true)
    private String sku;
    @NotBlank(message = "Product name should not be blank")
    private String name;
    private String description;
    @PositiveOrZero(message = "price should be positive or zero")
    private BigDecimal price;
    private  Integer stockQuantity;
    @NotNull(message = "please select category")
    private Long categoryId;
}
