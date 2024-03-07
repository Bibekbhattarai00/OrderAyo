package com.example.summerproject.exception;

public class NotFoundException extends RuntimeException {
    String message;

    @Override
    public String toString() {
        return message;
    }

    public NotFoundException(String message) {
        this.message = message;
    }

    public NotFoundException() {
        message = "Not found";
    }
}
