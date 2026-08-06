package com.wajeed.ecommerce.repository;

import com.wajeed.ecommerce.model.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
           SELECT p
           FROM Product p
           JOIN FETCH p.category
           """)
    Page<Product> findAllWithCategory(Pageable pageable);


    @Query("""
           SELECT p
           FROM Product p
           JOIN FETCH p.category
           WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
           """)
    List<Product> searchProductsWithCategory(
            @Param("keyword") String keyword);


    @Query("""
           SELECT p
           FROM Product p
           JOIN FETCH p.category
           WHERE p.category.id = :categoryId
           """)
    List<Product> findProductsByCategoryWithCategory(
            @Param("categoryId") Long categoryId);


    Optional<Product> findBySku(String sku);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :productId")
    Optional<Product> findByIdWithLock(
            @Param("productId") Long productId);
}