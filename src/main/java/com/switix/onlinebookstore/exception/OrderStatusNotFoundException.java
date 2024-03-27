package com.switix.onlinebookstore.exception;

public class OrderStatusNotFoundException extends RuntimeException {
    public OrderStatusNotFoundException(String message) {
        super(message);
    }
}
