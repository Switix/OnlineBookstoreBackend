package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.AuthorBookCountDto;
import com.switix.onlinebookstore.dto.CreateAuthorDto;
import com.switix.onlinebookstore.dto.UpdateAuthorDto;
import com.switix.onlinebookstore.exception.AuthorNotFoundException;
import com.switix.onlinebookstore.model.Author;
import com.switix.onlinebookstore.model.Book;
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
    public List<AuthorBookCountDto> countAllBooksMadeByAuthors() {
        return authorRepository.countBooksByAuthorOrderedByName();
    }

    @Override
    public void updateAuthor(UpdateAuthorDto updateAuthorDto) {
        Author author = authorRepository.findById(updateAuthorDto.getId())
                .orElseThrow(() -> new AuthorNotFoundException("Author not found"));

        author.setName(updateAuthorDto.getName());

        authorRepository.save(author);
    }

    @Override
    public void deleteAuthor(Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found"));

        // Remove references from Author to Book
        for (Book book : author.getAuthorBooks()) {
            book.getBookAuthors().remove(author);
        }

        // Clear references from Author
        author.getAuthorBooks().clear();

        // Save changes
        authorRepository.save(author);

        // Now delete the Author
        authorRepository.delete(author);
    }

    @Override
    public Author saveAuthor(CreateAuthorDto createAuthorDto) {
        Author author = mapToAuthor(createAuthorDto);
        return authorRepository.save(author);
    }

    @Override
    public Optional<Author> getAuthor(Long authorId) {
        return authorRepository.findById(authorId);
    }

    private Author mapToAuthor(CreateAuthorDto createAuthorDto) {
        Author author = new Author();
        author.setName(createAuthorDto.getName());

        return author;
    }
}
