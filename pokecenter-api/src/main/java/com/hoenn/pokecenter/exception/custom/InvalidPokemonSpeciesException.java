package com.hoenn.pokecenter.exception.custom;

public class InvalidPokemonSpeciesException extends RuntimeException {
    public InvalidPokemonSpeciesException(String species) {
        super("Invalid Pokemon species: " + species + ". Species not found in PokéAPI.");
    }
}
