package com.martin.bookstore.shared.exception;

public class CsvProcessingException extends RuntimeException {
    public CsvProcessingException(String message) {
        super(message);
    }

    public CsvProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
