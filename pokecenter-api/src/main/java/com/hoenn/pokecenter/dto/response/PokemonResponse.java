package com.hoenn.pokecenter.dto.response;

import com.hoenn.pokecenter.enums.PokemonCondition;
import com.hoenn.pokecenter.enums.PokemonStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PokemonResponse(
        String pokemonId,
        String name,
        String species,
        String trainerId,
        PokemonCondition condition,
        PokemonStatus status,
        LocalDateTime admissionDate,
        LocalDateTime releaseDate,
        String responsibleJoyId,
        String responsibleJoyName,
        LocalDateTime createdAt
) {}