package com.martin.bookstore.exception;

public class OrderPaymentNotAllowedException extends RuntimeException {
    public OrderPaymentNotAllowedException(String message) {
        super(message);
    }
}
