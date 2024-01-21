package com.switix.onlinebookstore.exception;

public class BookInsufficientStockException extends RuntimeException {
    public BookInsufficientStockException(String message) {
        super(message);
    }
}
