package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.AuthorBookCountDto;
import com.switix.onlinebookstore.dto.CreateAuthorDto;
import com.switix.onlinebookstore.dto.UpdateAuthorDto;
import com.switix.onlinebookstore.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<Author> getAuthorsByName(String name);
    List<AuthorBookCountDto> countAllBooksMadeByAuthors();

    void updateAuthor(UpdateAuthorDto updateAuthorDto);

    void deleteAuthor(Long authorId);

    Author saveAuthor(CreateAuthorDto createAuthorDto);

    Optional<Author> getAuthor(Long authorId);
}
