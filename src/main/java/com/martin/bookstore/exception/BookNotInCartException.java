package com.martin.bookstore.exception;

public class BookNotInCartException extends RuntimeException {
    public BookNotInCartException(String message) {
        super(message);
    }
}
