package com.martin.bookstore.exception;

public class BookMissingPriceException extends RuntimeException {
    public BookMissingPriceException(String message) {
        super(message);
    }
}
