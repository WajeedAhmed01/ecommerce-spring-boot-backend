package com.wajeed.ecommerce.repository;

import com.wajeed.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

   List<Order> findByUserId(Long userId);
}