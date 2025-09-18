package com.hoenn.pokecenter.dto.response;

import com.hoenn.pokecenter.enums.NurseJoyRole;

import java.time.LocalDateTime;

public record NurseJoyResponse(
   String nurseJoyId,
   String name,
   String email,
   String city,
   String region,
   NurseJoyRole role,
   LocalDateTime createdAt
) {}
