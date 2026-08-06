package com.wajeed.ecommerce.repository;

import com.wajeed.ecommerce.dto.CartRequest;
import com.wajeed.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long>
{
  Optional<Cart> findByUserId(Long user_id);

    @Query("""
       SELECT DISTINCT c
       FROM Cart c
       JOIN FETCH c.cartItems ci
       JOIN FETCH ci.product
       WHERE c.user.id = :userId
       """)
    Optional<Cart> findCartWithItemsAndProducts(
            @Param("userId") Long userId);
}
