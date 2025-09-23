package com.hoenn.pokemonleague.exceptions.custom;

public class ValidCityAlreadyExistsException extends RuntimeException {
    public ValidCityAlreadyExistsException(String message) {
        super(message);
    }
}
