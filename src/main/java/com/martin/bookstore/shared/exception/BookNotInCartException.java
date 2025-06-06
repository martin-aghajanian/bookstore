package com.martin.bookstore.shared.exception;

public class BookNotInCartException extends RuntimeException {
    public BookNotInCartException(String message) {
        super(message);
    }
}
