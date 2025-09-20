package com.hoenn.pokecenter.dto.request;

import com.hoenn.pokecenter.enums.PokemonCondition;
import com.hoenn.pokecenter.enums.PokemonStatus;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record PokemonUpdateRequest(
        @Size(max = 100, message = "Name must not exceed 100 characters")
        String name,

        @Size(max = 50, message = "Species must not exceed 50 characters")
        String species,

        String trainerId,
        PokemonCondition condition,
        PokemonStatus status,
        String responsibleJoyId,
        LocalDateTime releaseDate
) {}
