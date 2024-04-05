package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.CategoryBookCountDto;
import com.switix.onlinebookstore.dto.CreateCategoryDto;
import com.switix.onlinebookstore.dto.UpdateCategoryDto;
import com.switix.onlinebookstore.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    void updateCategory(UpdateCategoryDto updateCategoryDto);

    void deleteCategory(Long categoryId);

    Category saveCategory(CreateCategoryDto createCategoryDto);

    Optional<Category> getCategory(Long categoryId);

    List<CategoryBookCountDto> countBooksByCategory();

    List<Category> getCategoriesByName(String name);
}
