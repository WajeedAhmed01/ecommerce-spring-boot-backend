package com.wajeed.ecommerce.repository;

import com.wajeed.ecommerce.model.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>
{
    List<Product> findByNameContainingIgnoreCase(String keyword);
    List<Product> findByCategoryId(Long categoryId);

    Optional<Object> findBySku(String sku);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.id = :productId")
    Optional<Product> findByIdWithLock(@Param("productId") Long productId);
}
