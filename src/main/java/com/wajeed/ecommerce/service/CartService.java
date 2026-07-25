package com.wajeed.ecommerce.service;

import com.wajeed.ecommerce.dto.CartRequest;
import org.springframework.security.core.Authentication;

public interface CartService
{
    public void addProductToCart(Authentication authentication,
                                 CartRequest cartRequest);
}
