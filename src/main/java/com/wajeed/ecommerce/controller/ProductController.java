package com.wajeed.ecommerce.controller;

import com.wajeed.ecommerce.dto.ProductCreateRequest;
import com.wajeed.ecommerce.dto.ProductDto;
import com.wajeed.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> listAllProducts(
            @RequestParam(name = "pageNo", defaultValue = "0", required = false) Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        List<ProductDto> products = productService.listAllTheProducts(pageNo , pageSize, sortBy, sortDir);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> searchProductById(@PathVariable Long id) {
        ProductDto productDto = productService.findProductById(id);
        return new ResponseEntity<>(productDto , HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestParam(name = "keyword") String keyword) {
        List<ProductDto> products = productService.searchProductsByKeyword(keyword);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}