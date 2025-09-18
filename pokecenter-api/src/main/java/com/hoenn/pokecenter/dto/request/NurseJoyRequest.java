package com.hoenn.pokecenter.dto.request;

import com.hoenn.pokecenter.enums.NurseJoyRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NurseJoyRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "City is required")
        String city,

        @NotBlank(message = "Region is required")
        String region,

        @NotNull(message = "Role is required")
        NurseJoyRole role
) {}
