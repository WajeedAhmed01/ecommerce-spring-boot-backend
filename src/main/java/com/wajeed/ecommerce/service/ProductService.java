package com.wajeed.ecommerce.service;

import com.wajeed.ecommerce.dto.ProductCreateRequest;
import com.wajeed.ecommerce.dto.ProductDto;
import com.wajeed.ecommerce.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService
{
    public List<ProductDto>listAllTheProducts(int pageNo , int pageSize, String sortBy, String sortDir);
    public ProductDto findProductById(Long id);
    public ProductDto updateProduct(ProductCreateRequest productCreateRequest , Long id);
    public void deleteProductById(Long id);
    public ProductDto createProduct(ProductCreateRequest productCreateRequest);
    List<ProductDto> searchProductsByKeyword(String keyword);
    List<ProductDto> getProductsByCategory(Long categoryId);
}
