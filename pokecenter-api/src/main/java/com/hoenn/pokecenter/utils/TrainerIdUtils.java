package com.hoenn.pokecenter.utils;

import com.hoenn.pokecenter.exception.custom.InvalidTrainerIdFormatException;
import com.hoenn.pokemonleague.enums.TrainerRegion;

public final class TrainerIdUtils {

    private TrainerIdUtils() {}

    public static String extractRegion(String trainerId) {
        if (trainerId == null || !trainerId.matches("[A-Z]{2}\\d{6}")) {
            throw new InvalidTrainerIdFormatException(
                    "Invalid trainer ID format: '" + trainerId + "'. Expected format: [REGION][6DIGITS] (e.g., HN123456)"
            );
        }

        String region = trainerId.substring(0, 2);

        if (!isValidRegion(region)) {
            throw new InvalidTrainerIdFormatException(
                    "Unknown region: '" + region + "'. Valid regions: HN, KA, JO, SI, UN, KL, AL, GA, PA"
            );
        }

        return region;
    }

    public static boolean isFromRegion(String trainerId, String targetRegion) {
        return targetRegion.equals(extractRegion(trainerId));
    }

    public static boolean isFromHoenn(String trainerId) {
        return isFromRegion(trainerId, "HN");
    }

    private static boolean isValidRegion(String region) {
        try {
            TrainerRegion.getByRegionCode(region);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
