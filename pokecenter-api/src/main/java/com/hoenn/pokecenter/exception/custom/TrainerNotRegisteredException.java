package com.hoenn.pokecenter.exception.custom;

public class TrainerNotRegisteredException extends RuntimeException {
    public TrainerNotRegisteredException(String message) {
        super(message);
    }
}
