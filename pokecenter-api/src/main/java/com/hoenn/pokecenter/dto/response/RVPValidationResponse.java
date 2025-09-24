package com.hoenn.pokecenter.dto.response;

import java.time.LocalDate;

public record RVPValidationResponse(
        boolean hasValidRVP,
        String status,
        LocalDate expiryDate,
        String message
) {}