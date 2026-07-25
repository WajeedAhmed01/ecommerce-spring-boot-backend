package com.wajeed.ecommerce.repository;

import com.wajeed.ecommerce.model.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdempotencyKeyRepo extends JpaRepository<IdempotencyKey , String>
{

}
