package com.wajeed.ecommerce.service;

import com.wajeed.ecommerce.repository.CartItemRepo;
import com.wajeed.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImp {

    private final CartItemRepo cartItemRepo;
    private final ProductRepository productRepository;

    public CartItemServiceImp(CartItemRepo cartItemRepo, ProductRepository productRepository)
    {
        this.cartItemRepo = cartItemRepo;
        this.productRepository = productRepository;
    }
}