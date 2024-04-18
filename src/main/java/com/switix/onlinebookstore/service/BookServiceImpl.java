package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.SaveBookDto;
import com.switix.onlinebookstore.dto.UpdateBookDto;
import com.switix.onlinebookstore.exception.BookNotFoundException;
import com.switix.onlinebookstore.model.Book;
import com.switix.onlinebookstore.model.BookInventory;
import com.switix.onlinebookstore.repository.BookInventoryRepository;
import com.switix.onlinebookstore.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookInventoryRepository inventoryRepository;

    public BookServiceImpl(BookRepository bookRepository, BookInventoryRepository inventoryRepository) {
        this.bookRepository = bookRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public List<Book> getAllBooks(boolean isRemoved) {
        return bookRepository.findAllByIsRemoved(isRemoved);
    }

    @Override
    public List<Book> getAllBooksByCategory(Long categoryId, boolean isRemoved) {
        return bookRepository.findAllByCategory_IdAndIsRemoved(categoryId, isRemoved);
    }

    @Override
    public List<Book> getAllBooksByAuthor(Long authorId, boolean isRemoved) {
        return bookRepository.findAllByBookAuthors_IdAndIsRemoved(authorId, isRemoved);
    }

    @Override
    public Optional<Book> getBook(Long BookId) {
        return bookRepository.findById(BookId);
    }

    @Override
    public Book saveBook(SaveBookDto saveBookDto) {

        BookInventory inventory = inventoryRepository.save(saveBookDto.getInventory());
        Book book = new Book();
        book.setIsbn(saveBookDto.getIsbn());
        book.setCategory(saveBookDto.getCategory());
        book.setBookAuthors(saveBookDto.getBookAuthors());
        book.setInventory(inventory);
        book.setImageUrl(saveBookDto.getImageUrl());
        book.setPrice(saveBookDto.getPrice());
        book.setPublicationYear(saveBookDto.getPublicationYear());
        book.setDescription(saveBookDto.getDescription());
        book.setTitle(saveBookDto.getTitle());
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
        book.setIsRemoved(true);
        book.getInventory().setQuantity(0);
        bookRepository.save(book);
    }

    @Override
    public List<Book> getBooksByTittle(String searchQuery, boolean isRemoved) {
        return bookRepository.findAllByTitleIsLikeIgnoreCaseAndIsRemoved(searchQuery, isRemoved);
    }

    @Override
    public void updateBook(UpdateBookDto updateBookDto) {
        Book book = bookRepository.findById(updateBookDto.getId())
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        book.setIsbn(updateBookDto.getIsbn());
        book.setCategory(updateBookDto.getCategory());
        book.setBookAuthors(updateBookDto.getBookAuthors());
        book.setInventory(updateBookDto.getInventory());
        book.setImageUrl(updateBookDto.getImageUrl());
        book.setPrice(updateBookDto.getPrice());
        book.setPublicationYear(updateBookDto.getPublicationYear());
        book.setDescription(updateBookDto.getDescription());
        book.setTitle(updateBookDto.getTitle());

        bookRepository.save(book);


    }

}
