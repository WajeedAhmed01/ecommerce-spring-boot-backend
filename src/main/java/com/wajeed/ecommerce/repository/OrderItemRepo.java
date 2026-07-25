package com.wajeed.ecommerce.repository;

import com.wajeed.ecommerce.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepo extends JpaRepository<OrderItem , Long>
{

}
