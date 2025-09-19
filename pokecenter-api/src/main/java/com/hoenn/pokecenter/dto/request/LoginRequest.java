package com.hoenn.pokecenter.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Nurse Joy ID is required")
        String nurseJoyId,

        @NotBlank(message = "Password is required")
        String password
) {}
