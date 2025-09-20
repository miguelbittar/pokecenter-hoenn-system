package com.hoenn.pokecenter.dto.request;

import com.hoenn.pokecenter.enums.PokemonCondition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record PokemonRequest (

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must not exceed 100 characters")
        String name,

        @NotBlank(message = "Species is required")
        @Size(max = 50, message = "Species must not exceed 50 characters")
        String species,

        @NotBlank(message = "Trainer ID is required")
        String trainerId,

        @NotNull(message = "Condition is required")
        PokemonCondition condition,

        @NotBlank(message = "Responsible Joy ID is required")
        String responsibleJoyId
){}
