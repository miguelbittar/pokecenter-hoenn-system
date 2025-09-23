package com.hoenn.pokemonleague.dto.response;

import com.hoenn.pokemonleague.enums.TrainerRegion;

import java.time.LocalDateTime;

public record TrainerResponse(
        String trainerId,
        String name,
        String email,
        TrainerRegion region,
        LocalDateTime createdAt
) {}