package com.example.dreamshop.exceptions;

public class alreadyExistsException extends RuntimeException {
    public alreadyExistsException(String message) {
        super(message);
    }
}
