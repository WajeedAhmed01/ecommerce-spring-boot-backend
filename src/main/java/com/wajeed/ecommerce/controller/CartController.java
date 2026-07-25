package com.wajeed.ecommerce.controller;

import com.wajeed.ecommerce.dto.CartRequest;
import com.wajeed.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController
{

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService)
    {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addProductToCart(@RequestBody CartRequest cartRequest,
                                                   Authentication authentication)
    {
        cartService.addProductToCart(authentication, cartRequest);
        return new ResponseEntity<>("Product successfully added to cart!", HttpStatus.OK);
    }
}