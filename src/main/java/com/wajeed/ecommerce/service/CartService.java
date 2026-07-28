package com.wajeed.ecommerce.service;

import com.wajeed.ecommerce.dto.CartRequest;
import com.wajeed.ecommerce.dto.CartResponse;
import org.springframework.security.core.Authentication;

public interface CartService
{
    public void addProductToCart(Authentication authentication,
                                 CartRequest cartRequest);
    public CartResponse viewCart(Authentication authentication);
    public void removeCartItem(Authentication authentication,Long productId);
}
