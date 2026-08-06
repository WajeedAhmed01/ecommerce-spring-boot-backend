package com.wajeed.ecommerce.service;

import com.wajeed.ecommerce.dto.ProductCreateRequest;
import com.wajeed.ecommerce.dto.ProductDto;
import com.wajeed.ecommerce.exception.CategoryDoesNotExistException;
import com.wajeed.ecommerce.exception.ProductNotFoundException;
import com.wajeed.ecommerce.exception.SkuAlreadyExistException;
import com.wajeed.ecommerce.model.Category;
import com.wajeed.ecommerce.model.Product;
import com.wajeed.ecommerce.repository.CategoryRepository;
import com.wajeed.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public List<ProductDto> listAllTheProducts(
            int pageNo,
            int pageSize,
            String sortBy,
            String sortDir) {


        Sort sort;

        if(sortDir.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending();
        }
        else {
            sort = Sort.by(sortBy).descending();
        }


        List<Product> products =
                productRepository.findAllWithCategory(
                        PageRequest.of(pageNo,pageSize,sort)
                ).getContent();


        return convertToDto(products);
    }



    @Override
    public ProductDto findProductById(Long id) {

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new ProductNotFoundException(
                                        "Product not found with id: " + id));


        return convertSingleProduct(product);
    }



    @Override
    @Transactional
    public ProductDto updateProduct(
            ProductCreateRequest request,
            Long id) {


        Product existingProduct =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new ProductNotFoundException(
                                        "Product not found"));


        Category category =
                categoryRepository.findById(request.getCategoryId())
                        .orElseThrow(() ->
                                new CategoryDoesNotExistException(
                                        "Category not found"));


        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setStockQuantity(request.getStockQuantity());
        existingProduct.setCategory(category);


        productRepository.save(existingProduct);


        return convertSingleProduct(existingProduct);
    }



    @Override
    @Transactional
    public ProductDto createProduct(ProductCreateRequest request) {


        Category category =
                categoryRepository.findById(request.getCategoryId())
                        .orElseThrow(() ->
                                new CategoryDoesNotExistException(
                                        "Category does not exist"));


        productRepository.findBySku(request.getSku())
                .ifPresent(product -> {
                    throw new SkuAlreadyExistException(
                            "Product with this SKU already exists");
                });


        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setSku(request.getSku());
        product.setCategory(category);


        productRepository.save(product);


        return convertSingleProduct(product);
    }



    @Override
    public void deleteProductById(Long id) {


        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new ProductNotFoundException(
                                        "No such Product"));


        productRepository.delete(product);
    }



    @Override
    public List<ProductDto> searchProductsByKeyword(String keyword) {


        List<Product> products =
                productRepository.searchProductsWithCategory(keyword);


        return convertToDto(products);
    }



    @Override
    public List<ProductDto> getProductsByCategory(Long categoryId) {


        categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new CategoryDoesNotExistException(
                                "Category not found"));


        List<Product> products =
                productRepository.findProductsByCategoryWithCategory(
                        categoryId);


        if(products.isEmpty()) {
            throw new ProductNotFoundException(
                    "No Product found in this category");
        }


        return convertToDto(products);
    }



    private List<ProductDto> convertToDto(List<Product> products) {

        List<ProductDto> list = new ArrayList<>();

        for(Product product : products) {

            list.add(convertSingleProduct(product));
        }

        return list;
    }



    private ProductDto convertSingleProduct(Product product) {

        ProductDto dto = new ProductDto();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());


        if(product.getCategory()!=null) {

            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }


        return dto;
    }
}