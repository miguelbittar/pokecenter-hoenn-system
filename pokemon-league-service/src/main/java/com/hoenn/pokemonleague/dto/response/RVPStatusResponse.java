package com.hoenn.pokemonleague.dto.response;

public record RVPStatusResponse(
        boolean hasValidRVP,
        String status,
        String expiryDate,
        String message
) {}
