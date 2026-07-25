package com.wajeed.ecommerce.service;

import com.wajeed.ecommerce.dto.CategoryRequest;
import com.wajeed.ecommerce.exception.CategoryDoesNotExistException;
import com.wajeed.ecommerce.model.Category;
import com.wajeed.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService
{
    @Autowired
    private CategoryRepository categoryRepository;

    public Category saveCategory(CategoryRequest categoryRequest)
    {
        Category category = new Category();
        category.setName(categoryRequest.getName());

        categoryRepository.save(category);

        return category;
    }
    public List<Category> getAllCategories()
    {
        return categoryRepository.findAll();
    }
    public Category getCategoryById(Long id)
    {
       return categoryRepository.findById(id).orElseThrow(() -> new CategoryDoesNotExistException
                ("Category not found with id: " + id));
    }

}
