package com.wajeed.ecommerce.repository;

import com.wajeed.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

   List<Order> findByUserId(Long userId);

   Optional<Order> findByIdAndUser_Id(Long orderId , Long userId );

    @Query("""
       SELECT o
       FROM Order o
       JOIN FETCH o.orderItems oi
       JOIN FETCH oi.product
       WHERE o.user.id = :userId
       """)
    List<Order> findByUserIdGetOrderWithOrderItems(@Param("userId") Long userId);

    @Query("""
       SELECT o
       FROM Order o
       JOIN FETCH o.orderItems
       WHERE o.id = :orderId
       AND o.user.id = :userId
       """)
    Optional<Order> findByIdAndUserIdWithOrderItems(
            @Param("orderId") Long orderId,
            @Param("userId") Long userId);

    @Query("""
       SELECT DISTINCT o
       FROM Order o
       JOIN FETCH o.orderItems oi
       JOIN FETCH oi.product
       WHERE o.id = :orderId
       AND o.user.id = :userId
       """)
    Optional<Order> findByIdAndUserIdWithOrderItemsAndProducts(
            @Param("orderId") Long orderId,
            @Param("userId") Long userId);
}