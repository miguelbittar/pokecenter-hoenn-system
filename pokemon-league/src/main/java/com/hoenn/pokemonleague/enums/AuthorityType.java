package com.hoenn.pokemonleague.enums;

public enum AuthorityType {
    GYM("Gym Leader"),
    POKECENTER("Nurse Joy"),
    POKEMON_LEAGUE("League Official");

    private final String displayName;

    AuthorityType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
