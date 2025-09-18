package com.hoenn.pokecenter.exception.custom;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {

        super(message);
    }
}
