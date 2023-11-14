package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.AuthorBookCountDto;
import com.switix.onlinebookstore.model.Author;
import com.switix.onlinebookstore.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<Author> getAuthorsByName(String name) {
        name = "%" + name + "%";
        return authorRepository.findByNameLikeIgnoreCase(name);
    }

    @Override
    public Optional<Author> getAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    @Override
    public List<AuthorBookCountDto> countAllBooksMadeByAuthors() {
        return authorRepository.countBooksByAuthorOrderedByName();
    }
}
