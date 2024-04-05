package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.CategoryBookCountDto;
import com.switix.onlinebookstore.dto.CreateCategoryDto;
import com.switix.onlinebookstore.dto.UpdateCategoryDto;
import com.switix.onlinebookstore.exception.CategoryNotFoundException;
import com.switix.onlinebookstore.model.Book;
import com.switix.onlinebookstore.model.Category;
import com.switix.onlinebookstore.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public void updateCategory(UpdateCategoryDto updateCategoryDto) {
        Category category = categoryRepository.findById(updateCategoryDto.getId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        category.setName(updateCategoryDto.getName());

        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        for (Book book : category.getCategoryBooks()) {
            book.setCategory(null);
        }

        category.getCategoryBooks().clear();

        categoryRepository.save(category);

        categoryRepository.delete(category);

    }

    @Override
    public Category saveCategory(CreateCategoryDto createCategoryDto) {
        Category category = mapToCategory(createCategoryDto);
        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    public List<CategoryBookCountDto> countBooksByCategory() {
        return categoryRepository.countBooksByCategory();
    }

    @Override
    public List<Category> getCategoriesByName(String name) {
        return categoryRepository.findAllByNameLikeIgnoreCase(name);
    }


    private Category mapToCategory(CreateCategoryDto createCategoryDto) {
        Category category = new Category();
        category.setName(createCategoryDto.getName());
        return category;
    }

}
