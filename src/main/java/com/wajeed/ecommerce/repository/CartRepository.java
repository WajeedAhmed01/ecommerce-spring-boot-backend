package com.wajeed.ecommerce.repository;

import com.wajeed.ecommerce.dto.CartRequest;
import com.wajeed.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long>
{
  Optional<Cart> findByUserId(Long user_id);
}
