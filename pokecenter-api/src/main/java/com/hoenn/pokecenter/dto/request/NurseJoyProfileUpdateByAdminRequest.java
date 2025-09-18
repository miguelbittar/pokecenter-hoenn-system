package com.hoenn.pokecenter.dto.request;

import com.hoenn.pokecenter.enums.NurseJoyRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record NurseJoyProfileUpdateByAdminRequest(
        @Size(max = 100, message = "Name must not exceed 100 characters")
        String name,

        @Email(message = "Email should be valid")
        @Size(max = 150, message = "Email must not exceed 150 characters")
        String email,

        String password,

        @Size(max = 85, message = "City must not exceed 85 characters")
        String city,

        @Size(max = 50, message = "Region must not exceed 50 characters")
        String region,

        NurseJoyRole role
) {}
