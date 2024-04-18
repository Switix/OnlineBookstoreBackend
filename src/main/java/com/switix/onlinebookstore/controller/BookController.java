package com.switix.onlinebookstore.controller;

import com.switix.onlinebookstore.dto.SaveBookDto;
import com.switix.onlinebookstore.dto.UpdateBookDto;
import com.switix.onlinebookstore.exception.BookNotFoundException;
import com.switix.onlinebookstore.exception.CategoryNotFoundException;
import com.switix.onlinebookstore.model.Book;
import com.switix.onlinebookstore.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getBooks() {
        return bookService.getAllBooks(false);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Book> getBook(@PathVariable Long bookId) {
        return ResponseEntity.of(bookService.getBook(bookId));
    }

    @GetMapping("categories/{categoryId}")
    public List<Book> getBooksByCategory(@PathVariable Long categoryId) {
        return bookService.getAllBooksByCategory(categoryId,false);
    }

    @GetMapping("authors/{authorId}")
    public List<Book> getBooksMadeByAuthor(@PathVariable Long authorId) {
        return bookService.getAllBooksByAuthor(authorId,false);
    }

    @PostMapping("/admin")
    public ResponseEntity<Void> saveBook(@RequestBody SaveBookDto saveBookDto) {
        Book savedBook = bookService.saveBook(saveBookDto);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/books/{bookId}")
                .buildAndExpand(savedBook.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/admin")
    public ResponseEntity<Void> updateBook(@RequestBody UpdateBookDto updateBookDto) {
        try {
            bookService.updateBook(updateBookDto);
            return ResponseEntity.noContent().build();
        } catch (CategoryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping("/admin/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        try {
            bookService.deleteBook(bookId);
            return ResponseEntity.noContent().build();
        }
        catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }

    }

}
