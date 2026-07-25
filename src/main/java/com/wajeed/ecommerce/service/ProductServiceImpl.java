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
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<ProductDto> listAllTheProducts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = null;

        if (sortDir.equalsIgnoreCase("Asc")) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }

        List<Product> products = productRepository.findAll(PageRequest.of(pageNo, pageSize, sort))
                .getContent();


        List<ProductDto> productDtos = new ArrayList<>();


        for (Product product : products) {
            ProductDto dto = new ProductDto();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setDescription(product.getDescription());
            dto.setPrice(product.getPrice());
            dto.setStockQuantity(product.getStockQuantity());

            if (product.getCategory() != null) {
                dto.setCategoryId(product.getCategory().getId());
                dto.setCategoryName(product.getCategory().getName());
            }

            productDtos.add(dto);
        }


        return productDtos;
    }


    @Override
    public ProductDto findProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        ProductDto productDto = new ProductDto();

        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setStockQuantity(product.getStockQuantity());
        productDto.setCategoryId(product.getCategory().getId());
        productDto.setCategoryName(product.getCategory().getName()); // This keeps it flat!

        // 4. Return the clean DTO
        return productDto;
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductCreateRequest productCreateRequest, Long id) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException
                ("Product not found with id"));
        Category category = categoryRepository.findById(productCreateRequest.getCategoryId()).orElseThrow(() -> new
                CategoryDoesNotExistException
                ("Category not found with id"));

        existingProduct.setName(productCreateRequest.getName());
        existingProduct.setDescription(productCreateRequest.getDescription());
        existingProduct.setPrice(productCreateRequest.getPrice());
        existingProduct.setStockQuantity(productCreateRequest.getStockQuantity());
        existingProduct.setCategory(category);

        productRepository.save(existingProduct);

        ProductDto productDto = new ProductDto();

        productDto.setId(existingProduct.getId());
        productDto.setName(existingProduct.getName());
        productDto.setDescription(existingProduct.getDescription());
        productDto.setCategoryId(category.getId());
        productDto.setCategoryName(category.getName());
        productDto.setStockQuantity(existingProduct.getStockQuantity());
        productDto.setPrice(existingProduct.getPrice());

        return productDto;
    }

    @Override
    public void deleteProductById(@PathVariable Long id) {

        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException
                ("No such Product: "));

        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductCreateRequest productCreateRequest) {
        Category existingCategory = categoryRepository.findById(productCreateRequest.getCategoryId())
                .orElseThrow(() -> new CategoryDoesNotExistException
                        ("Category does not exist"));

        productRepository.findBySku(productCreateRequest.getSku())
                .ifPresent(product -> {
                    throw new SkuAlreadyExistException("Product with this SKU already exists");
                });

        Product product = new Product();
        product.setPrice(productCreateRequest.getPrice());
        product.setName(productCreateRequest.getName());
        product.setStockQuantity(productCreateRequest.getStockQuantity());
        product.setDescription(productCreateRequest.getDescription());
        product.setSku(productCreateRequest.getSku());
        product.setCategory(existingCategory);

        productRepository.save(product);

        ProductDto productDto = new ProductDto();

        productDto.setDescription(product.getDescription());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setStockQuantity(product.getStockQuantity());
        productDto.setId(product.getId());
        productDto.setCategoryId(product.getCategory().getId());
        productDto.setCategoryName(product.getCategory().getName());

        return productDto;
    }

    public List<ProductDto> searchProductsByKeyword(String keyword) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
        List<ProductDto> productDtos = new ArrayList<>();


        for (Product product : products) {
            ProductDto dto = new ProductDto();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setDescription(product.getDescription());
            dto.setPrice(product.getPrice());
            dto.setStockQuantity(product.getStockQuantity());

            if (product.getCategory() != null) {
                dto.setCategoryId(product.getCategory().getId());
                dto.setCategoryName(product.getCategory().getName());
            }

            productDtos.add(dto);
        }
        return productDtos;
    }
    public List<ProductDto> getProductsByCategory(Long categoryId)
    {
        categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new CategoryDoesNotExistException("Category not found"));

        List <Product> products = productRepository.findByCategoryId(categoryId);

        if(products.isEmpty())
        {
            throw new ProductNotFoundException("No Product found in this category");
        }

        List<ProductDto> productDtos = new ArrayList<>();


        for (Product product : products) {
            ProductDto dto = new ProductDto();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setDescription(product.getDescription());
            dto.setPrice(product.getPrice());
            dto.setStockQuantity(product.getStockQuantity());

            if (product.getCategory() != null) {
                dto.setCategoryId(product.getCategory().getId());
                dto.setCategoryName(product.getCategory().getName());
            }

            productDtos.add(dto);
        }
        return productDtos;
    }
}