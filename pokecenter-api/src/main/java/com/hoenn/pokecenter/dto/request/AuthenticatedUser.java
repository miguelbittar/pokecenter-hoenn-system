package com.hoenn.pokecenter.dto.request;

public record AuthenticatedUser(
        String nurseJoyId,
        String name,
        String email,
        String role
) {}
