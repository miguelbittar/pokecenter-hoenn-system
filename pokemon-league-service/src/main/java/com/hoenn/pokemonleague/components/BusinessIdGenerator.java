package com.hoenn.pokemonleague.components;

import com.hoenn.pokemonleague.enums.TrainerRegion;
import com.hoenn.pokemonleague.repository.RVPRepository;
import com.hoenn.pokemonleague.repository.TrainerRepository;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class BusinessIdGenerator {

    private final TrainerRepository trainerRepository;
    private final RVPRepository rvpRepository;
    private final SecureRandom secureRandom;

    public BusinessIdGenerator(TrainerRepository trainerRepository, RVPRepository rvpRepository) {
        this.trainerRepository = trainerRepository;
        this.rvpRepository = rvpRepository;
        this.secureRandom = new SecureRandom();
    }

    public String generateRandomTrainerId(TrainerRegion region) {
        String trainerId;
        int attempts = 0;
        int maxAttempts = 10;

        do {
            int randomNumber = secureRandom.nextInt(900000) + 100000;
            trainerId = region.getRegionCode() + randomNumber;
            attempts++;

            if (attempts > maxAttempts) {
                throw new RuntimeException("Unable to generate unique trainer ID after " + maxAttempts + " attempts");
            }
        } while (trainerRepository.existsByTrainerIdAndIsDeletedFalse(trainerId));

        return trainerId;
    }

    public String generateRandomRVPId(TrainerRegion targetRegion) {
        String rvpId;
        int attempts = 0;
        int maxAttempts = 10;

        do {
            int randomNumber = secureRandom.nextInt(900000) + 100000;
            rvpId = "RVP" + randomNumber + "-" + targetRegion.getRegionCode();
            attempts++;

            if (attempts > maxAttempts) {
                throw new RuntimeException("Unable to generate unique RVP ID after " + maxAttempts + " attempts");
            }
        } while (rvpRepository.existsByRvpIdAndIsDeletedFalse(rvpId));

        return rvpId;
    }
}
