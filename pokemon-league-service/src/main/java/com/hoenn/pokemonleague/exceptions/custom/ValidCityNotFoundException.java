package com.hoenn.pokemonleague.exceptions.custom;

public class ValidCityNotFoundException extends RuntimeException {
    public ValidCityNotFoundException(String message) {
        super(message);
    }
}
