package com.martin.bookstore.shared.exception;

public class OrderCancellationNotAllowedException extends RuntimeException {
    public OrderCancellationNotAllowedException(String message) {
        super(message);
    }
}
