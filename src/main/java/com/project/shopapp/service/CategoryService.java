package com.project.shopapp.service;

import com.project.shopapp.dto.CategoryDTO;
import com.project.shopapp.model.Category;
import com.project.shopapp.respository.CategoryRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRespository categoryRespository;

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category newCategory = Category.builder().name(categoryDTO.getName()).build();
        return categoryRespository.save(newCategory);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRespository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRespository.findAll();
    }

    @Override
    public Category updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category1 = getCategoryById(id);
        if (category1 != null) {
            category1.setName(categoryDTO.getName());
            return categoryRespository.save(category1);
        }
        return null;
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRespository.deleteById(id);
    }
}
