package com.example.summerproject.exception;

public class UnauthorizedAccessException extends Exception{
    String message;


    @Override
    public String toString() {
        return message;
    }

    public UnauthorizedAccessException(String message) {
        this.message = message;
    }

    public UnauthorizedAccessException() {
        message = "Type Mismatched";
    }
}
