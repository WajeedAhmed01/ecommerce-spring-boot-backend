package com.wajeed.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CartRequest {

    @JsonProperty("product_id")
    private Long productId;

    private Integer quantity;
}