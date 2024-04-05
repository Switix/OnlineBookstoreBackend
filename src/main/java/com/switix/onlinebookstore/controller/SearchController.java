package com.switix.onlinebookstore.controller;


import com.switix.onlinebookstore.dto.AuthorBookCountDto;
import com.switix.onlinebookstore.dto.SearchDto;
import com.switix.onlinebookstore.model.Author;
import com.switix.onlinebookstore.model.Category;
import com.switix.onlinebookstore.service.AuthorService;
import com.switix.onlinebookstore.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/search")
public class SearchController {

    private final AuthorService authorService;
    private final CategoryService categoryService;

    public SearchController(AuthorService authorService, CategoryService categoryService) {
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @GetMapping(params = "q")
    public SearchDto searchSuggestion(@RequestParam String q) {
        String searchQuery = "%" + q + "%";
        SearchDto result = new SearchDto();
        result.setSuggestedAuthors(authorService.getAuthorsByName(searchQuery));
        result.setSuggestedCategories(categoryService.getCategoriesByName(searchQuery));
        return result;
    }

    @GetMapping("authors")
    public List<AuthorBookCountDto> getAuthors() {
        return authorService.countAllBooksMadeByAuthors();

    }

    @GetMapping(path = "authors", params = "q")
    public List<Author> searchAuthorsByName(@RequestParam String q) {
        String searchQuery = "%" + q + "%";
        return authorService.getAuthorsByName(searchQuery);
    }

    @GetMapping(path = "categories", params = "q")
    public List<Category> searchCategoriesByName(@RequestParam String q) {
        String searchQuery = "%" + q + "%";
        return categoryService.getCategoriesByName(searchQuery);
    }
}
