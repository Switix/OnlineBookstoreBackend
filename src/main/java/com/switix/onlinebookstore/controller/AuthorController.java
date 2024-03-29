package com.switix.onlinebookstore.controller;

import com.switix.onlinebookstore.dto.CreateAuthorDto;
import com.switix.onlinebookstore.dto.UpdateAuthorDto;
import com.switix.onlinebookstore.exception.AuthorNotFoundException;
import com.switix.onlinebookstore.model.Author;
import com.switix.onlinebookstore.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/authors")
public class AuthorController {

    private final AuthorService authorService;


    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }


    @GetMapping("/{authorId}")
    public ResponseEntity<Author> getAuthor(@PathVariable Long authorId) {
        return ResponseEntity.of(authorService.getAuthor(authorId));
    }

    @PatchMapping("admin")
    public ResponseEntity<Void> updateAuthor(@RequestBody UpdateAuthorDto updateAuthorDto){
        try {
            authorService.updateAuthor(updateAuthorDto);
            return ResponseEntity.noContent().build();
        } catch (AuthorNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
    @DeleteMapping("admin/{authorId}")
    public ResponseEntity<Void> deleteAuthor( @PathVariable Long authorId) {
        try {
            authorService.deleteAuthor(authorId);
            return ResponseEntity.noContent().build();
        } catch (AuthorNotFoundException  e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
    @PostMapping("admin")
    public ResponseEntity<Void> saveAuthor(@RequestBody CreateAuthorDto createAuthorDto) {
       Author savedAuthor =authorService.saveAuthor(createAuthorDto);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/authors/{authorId}")
                .buildAndExpand(savedAuthor.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
