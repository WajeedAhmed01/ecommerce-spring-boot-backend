package com.wajeed.ecommerce.service;

import com.wajeed.ecommerce.dto.CategoryRequest;
import com.wajeed.ecommerce.model.Category;

import java.util.List;

public interface CategoryService
{
    public Category saveCategory(CategoryRequest categoryRequest);
    public List<Category> getAllCategories();
    public Category getCategoryById(Long id);

}
