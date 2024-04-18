package com.switix.onlinebookstore.dto;


import com.switix.onlinebookstore.model.Author;
import com.switix.onlinebookstore.model.BookInventory;
import com.switix.onlinebookstore.model.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class SaveBookDto {

    private String title;

    private String description;

    private BigDecimal price;


    private String imageUrl;

    private int publicationYear;

    private String isbn;

    private Category category;

    private BookInventory inventory;

    private Set<Author> bookAuthors = new HashSet<>();

}
