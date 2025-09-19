package com.hoenn.pokecenter.dto.response;

import com.hoenn.pokecenter.enums.NurseJoyRole;

public record LoginResponse(
        String token,
        String nurseJoyId,
        NurseJoyRole role
) {}
