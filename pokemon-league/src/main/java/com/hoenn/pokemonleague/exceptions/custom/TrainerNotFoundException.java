package com.hoenn.pokemonleague.exceptions.custom;

public class TrainerNotFoundException extends RuntimeException {
    public TrainerNotFoundException(String message) {
        super(message);
    }
}
