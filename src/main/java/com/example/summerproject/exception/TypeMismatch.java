package com.example.summerproject.exception;

public class TypeMismatch extends Exception{
    String message;

    @Override
    public String toString() {
        return message;
    }

    public TypeMismatch(String message) {
        this.message = message;
    }

    public TypeMismatch() {
        message = "Type Mismatched";
    }
}
