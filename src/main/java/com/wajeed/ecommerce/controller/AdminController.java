package com.wajeed.ecommerce.controller;

import com.wajeed.ecommerce.dto.ProductCreateRequest;
import com.wajeed.ecommerce.dto.ProductDto;
import com.wajeed.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class AdminController
{
    public ProductService productService;

    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductDto> createProductRequest
            (@Valid @RequestBody ProductCreateRequest productCreateRequest) {
        ProductDto productDto = productService.createProduct(productCreateRequest);
        return new ResponseEntity<>(productDto , HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductCreateRequest productCreateRequest,
                                                    @PathVariable Long id) {
        ProductDto updatedProduct = productService.updateProduct(productCreateRequest, id);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return new ResponseEntity<>("Product Deleted successfully!" , HttpStatus.OK);
    }
}
