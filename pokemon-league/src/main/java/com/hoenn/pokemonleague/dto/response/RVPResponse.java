package com.hoenn.pokemonleague.dto.response;

import com.hoenn.pokemonleague.enums.RVPStatus;
import com.hoenn.pokemonleague.enums.TrainerRegion;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record RVPResponse(
        String rvpId,
        String trainerId,
        String trainerName,
        TrainerRegion targetRegion,
        String issuingCityName,
        RVPStatus status,
        LocalDate issueDate,
        LocalDate expiryDate,
        LocalDateTime createdAt
) {}
