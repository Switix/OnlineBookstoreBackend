package com.switix.onlinebookstore.controller;

import com.switix.onlinebookstore.dto.CategoryBookCountDto;
import com.switix.onlinebookstore.model.Category;
import com.switix.onlinebookstore.repository.CategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categories")
@CrossOrigin(origins = "http://localhost:8081")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategory(@PathVariable Long categoryId){
        return ResponseEntity.of(categoryRepository.findById(categoryId));
    }
    @GetMapping
    public List<CategoryBookCountDto> getCategories() {
        return categoryRepository.countBooksByCategory();
    }
}
