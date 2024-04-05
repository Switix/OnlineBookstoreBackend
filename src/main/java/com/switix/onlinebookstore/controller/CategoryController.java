package com.switix.onlinebookstore.controller;

import com.switix.onlinebookstore.dto.CategoryBookCountDto;
import com.switix.onlinebookstore.dto.CreateCategoryDto;
import com.switix.onlinebookstore.dto.UpdateCategoryDto;
import com.switix.onlinebookstore.exception.CategoryNotFoundException;
import com.switix.onlinebookstore.model.Category;
import com.switix.onlinebookstore.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategory(@PathVariable Long categoryId) {
        return ResponseEntity.of(categoryService.getCategory(categoryId));
    }

    @GetMapping
    public List<CategoryBookCountDto> getCategories() {
        return categoryService.countBooksByCategory();
    }

    @PatchMapping("admin")
    public ResponseEntity<Void> updateCategory(@RequestBody UpdateCategoryDto updateCategoryDto) {
        try {
            categoryService.updateCategory(updateCategoryDto);
            return ResponseEntity.noContent().build();
        } catch (CategoryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping("admin/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.noContent().build();
        } catch (CategoryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping("admin")
    public ResponseEntity<Void> saveCategory(@RequestBody CreateCategoryDto createCategoryDto) {
        Category saveCategory = categoryService.saveCategory(createCategoryDto);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/categories/{categoryId}")
                .buildAndExpand(saveCategory.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
