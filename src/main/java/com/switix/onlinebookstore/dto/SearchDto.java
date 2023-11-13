package com.switix.onlinebookstore.dto;

import com.switix.onlinebookstore.model.Author;
import com.switix.onlinebookstore.model.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SearchDto {
    List<Author> suggestedAuthors;
    List<Category> suggestedCategories;
}
