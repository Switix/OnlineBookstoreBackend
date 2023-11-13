package com.switix.onlinebookstore.controller;

import com.switix.onlinebookstore.model.Book;
import com.switix.onlinebookstore.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/books")
@CrossOrigin(origins = "http://localhost:8081")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Book> getBook(@PathVariable Long bookId) {
        return ResponseEntity.of(bookService.getBook(bookId));
    }

    @GetMapping("categories/{categoryId}")
    public List<Book> getBooksByCategory(@PathVariable Long categoryId) {
        return bookService.getAllBooksByCategory(categoryId);
    }

    @GetMapping("authors/{authorId}")
    public List<Book> getBooksMadeByAuthor(@PathVariable Long authorId) {
        return bookService.getAllBooksByAuthor(authorId);
    }

    @PostMapping
    public ResponseEntity<Void> saveBook(@RequestBody Book book) {
        Book savedBook = bookService.saveBook(book);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{bookId}").buildAndExpand(savedBook.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

}
