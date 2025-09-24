package com.hoenn.pokecenter.exception.custom;

public class InvalidTrainerIdFormatException extends RuntimeException {
    public InvalidTrainerIdFormatException(String message) {
        super(message);
    }
}
