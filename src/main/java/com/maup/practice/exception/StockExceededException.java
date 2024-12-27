package com.maup.practice.exception;

public class StockExceededException extends RuntimeException {
    public StockExceededException(String message) {
        super(message);
    }
}
