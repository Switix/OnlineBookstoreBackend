package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<Author> getAuthorsByName(String name);
    Optional<Author> getAuthorById(Long id);
}
