package com.martin.bookstore.shared.exception;

public class BookMissingPriceException extends RuntimeException {
    public BookMissingPriceException(String message) {
        super(message);
    }
}
