package com.hoenn.pokemonleague.exceptions.custom;

public class InvalidRVPRequestException extends RuntimeException {
    public InvalidRVPRequestException(String message) {
        super(message);
    }
}
