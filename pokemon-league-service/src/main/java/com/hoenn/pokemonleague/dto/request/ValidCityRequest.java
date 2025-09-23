package com.hoenn.pokemonleague.dto.request;

import com.hoenn.pokemonleague.enums.AuthorityType;
import com.hoenn.pokemonleague.enums.TrainerRegion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ValidCityRequest(

        @NotBlank(message = "City name is required")
        @Size(max = 100, message = "City name must not exceed 100 characters")
        String cityName,

        @NotNull(message = "Region is required")
        TrainerRegion region,

        @NotNull(message = "Authority type is required")
        AuthorityType authorityType

) {}
