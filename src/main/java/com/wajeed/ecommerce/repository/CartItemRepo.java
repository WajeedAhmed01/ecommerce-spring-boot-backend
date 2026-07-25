package com.wajeed.ecommerce.repository;

import com.wajeed.ecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem, Long>
{
    void deleteByCartId(Long CartId);


}
