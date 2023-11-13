package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.model.Book;

import java.util.List;
import java.util.Optional;


public interface BookService {
    List<Book> getAllBooks();
    List<Book> getAllBooksByCategory(Long categoryId);
    List<Book> getAllBooksByAuthor(Long authorId);
    Optional<Book> getBook(Long BookId);
    Book saveBook(Book book);
    void deleteBook(Long bookId);

}
