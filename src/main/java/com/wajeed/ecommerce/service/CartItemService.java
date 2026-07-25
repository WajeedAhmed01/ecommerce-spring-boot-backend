package com.wajeed.ecommerce.service;

import com.wajeed.ecommerce.model.Cart;
import com.wajeed.ecommerce.model.CartItem;

public interface CartItemService
{
    public CartItem createCartItem(Cart cart, Long productId, Integer quantity);
}
