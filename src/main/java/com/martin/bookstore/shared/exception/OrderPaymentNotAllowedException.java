package com.martin.bookstore.shared.exception;

public class OrderPaymentNotAllowedException extends RuntimeException {
    public OrderPaymentNotAllowedException(String message) {
        super(message);
    }
}
