package com.martin.bookstore.shared.exception;

public class InvalidOrderQuantityException extends RuntimeException {
    public InvalidOrderQuantityException(String message) {
        super(message);
    }
}
