package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.SaveBookDto;
import com.switix.onlinebookstore.dto.UpdateBookDto;
import com.switix.onlinebookstore.model.Book;

import java.util.List;
import java.util.Optional;


public interface BookService {
    List<Book> getAllBooks(boolean isRemoved);
    List<Book> getAllBooksByCategory(Long categoryId,boolean isRemoved);
    List<Book> getAllBooksByAuthor(Long authorId,boolean isRemoved);
    List<Book> getBooksByTittle(String searchQuery,boolean isRemoved);
    Optional<Book> getBook(Long BookId);
    Book saveBook(SaveBookDto saveBookDto);
    void deleteBook(Long bookId);



    void updateBook(UpdateBookDto updateBookDto);
}
