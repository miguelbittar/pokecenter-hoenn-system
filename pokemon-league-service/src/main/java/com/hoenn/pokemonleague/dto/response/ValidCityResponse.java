package com.hoenn.pokemonleague.dto.response;

import com.hoenn.pokemonleague.enums.AuthorityType;
import com.hoenn.pokemonleague.enums.TrainerRegion;

import java.time.LocalDateTime;

public record ValidCityResponse(
        String id,
        String cityName,
        TrainerRegion region,
        AuthorityType authorityType,
        String authorityName,
        LocalDateTime createdAt
) {}