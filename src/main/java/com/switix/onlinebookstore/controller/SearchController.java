package com.switix.onlinebookstore.controller;


import com.switix.onlinebookstore.dto.AuthorBookCountDto;
import com.switix.onlinebookstore.dto.CategoryBookCountDto;
import com.switix.onlinebookstore.dto.SearchDto;
import com.switix.onlinebookstore.repository.CategoryRepository;
import com.switix.onlinebookstore.service.AuthorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/search")
@CrossOrigin(origins = "http://localhost:8081")
public class SearchController {

    private final AuthorService authorService;
    private final CategoryRepository categoryRepository;

    public SearchController(AuthorService authorService, CategoryRepository categoryRepository) {
        this.authorService = authorService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping(params = "q")
    public SearchDto searchSuggestion(@RequestParam String q) {
        String searchQuery = "%" + q + "%";
        SearchDto result = new SearchDto();
        result.setSuggestedAuthors(authorService.getAuthorsByName(searchQuery));
        result.setSuggestedCategories(categoryRepository.findAllByNameLikeIgnoreCase(searchQuery));
        return result;
    }
    @GetMapping("categories")
    public List<CategoryBookCountDto> getCategories() {
        return categoryRepository.countBooksByCategory();

    }
    @GetMapping("authors")
    public List<AuthorBookCountDto> getAuthors() {
        return authorService.countAllBooksMadeByAuthors();

    }
}
