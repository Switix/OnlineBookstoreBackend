package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.model.Book;
import com.switix.onlinebookstore.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> getAllBooksByCategory(Long categoryId) {
        return bookRepository.findAllByCategory_Id(categoryId);
    }

    @Override
    public List<Book> getAllBooksByAuthor(Long authorId) {
        return bookRepository.findAllByBookAuthors_Id(authorId);
    }

    @Override
    public Optional<Book> getBook(Long BookId) {
        return bookRepository.findById(BookId);
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }

}
