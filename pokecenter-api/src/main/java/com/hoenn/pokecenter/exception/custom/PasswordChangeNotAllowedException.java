package com.hoenn.pokecenter.exception.custom;

public class PasswordChangeNotAllowedException extends RuntimeException {
    public PasswordChangeNotAllowedException(String message) {
        super(message);
    }
}
