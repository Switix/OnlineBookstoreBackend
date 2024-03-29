package com.switix.onlinebookstore.exception;

public class AuthorNotFoundException  extends RuntimeException {
    public AuthorNotFoundException(String message) {
        super(message);
    }
}
