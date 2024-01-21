package com.switix.onlinebookstore.exception;

public class BillingAddressNotFoundException extends RuntimeException {
    public BillingAddressNotFoundException(String message) {
        super(message);
    }
}
