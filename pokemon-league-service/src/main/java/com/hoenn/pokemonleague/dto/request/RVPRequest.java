package com.hoenn.pokemonleague.dto.request;

import com.hoenn.pokemonleague.enums.TrainerRegion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RVPRequest(
        @NotBlank(message = "Trainer ID is required")
        String trainerId,

        @NotNull(message = "Target region is required")
        TrainerRegion targetRegion,

        @NotBlank(message = "City name is required")
        String cityName
) {}