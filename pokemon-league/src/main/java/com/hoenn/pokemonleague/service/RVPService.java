package com.hoenn.pokemonleague.service;

import com.hoenn.pokemonleague.components.BusinessIdGenerator;
import com.hoenn.pokemonleague.entity.RVP;
import com.hoenn.pokemonleague.entity.Trainer;
import com.hoenn.pokemonleague.entity.ValidCity;
import com.hoenn.pokemonleague.enums.TrainerRegion;
import com.hoenn.pokemonleague.exceptions.custom.DuplicateRVPException;
import com.hoenn.pokemonleague.exceptions.custom.InvalidRVPRequestException;
import com.hoenn.pokemonleague.exceptions.custom.TrainerNotFoundException;
import com.hoenn.pokemonleague.exceptions.custom.ValidCityNotFoundException;
import com.hoenn.pokemonleague.repository.RVPRepository;
import com.hoenn.pokemonleague.repository.TrainerRepository;
import com.hoenn.pokemonleague.repository.ValidCityRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RVPService {

    private final RVPRepository rvpRepository;
    private final TrainerRepository trainerRepository;
    private final ValidCityRepository validCityRepository;
    private final BusinessIdGenerator businessIdGenerator;

    public RVPService(RVPRepository rvpRepository, TrainerRepository trainerRepository,
                      ValidCityRepository validCityRepository, BusinessIdGenerator businessIdGenerator) {
        this.rvpRepository = rvpRepository;
        this.trainerRepository = trainerRepository;
        this.validCityRepository = validCityRepository;
        this.businessIdGenerator = businessIdGenerator;
    }

    public RVP issueRVP(String trainerId, TrainerRegion targetRegion, String issuingCityName){
        Trainer trainer = trainerRepository.findByTrainerIdAndIsDeletedFalse(trainerId)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not found: " + trainerId));

        if (trainer.getRegion() == targetRegion){
            throw new InvalidRVPRequestException("Cannot issue RVP for trainer's own region");
        }

        Optional<RVP> existingRVP = rvpRepository.findActiveRVPByTrainerAndTargetRegion(trainerId, targetRegion);
        if (existingRVP.isPresent()){
            throw new DuplicateRVPException("Trainer already has active RVP for region: " + targetRegion);
        }

        ValidCity issuingCity = validCityRepository.findByCityNameAndIsDeletedFalse(issuingCityName)
                .orElseThrow(() -> new ValidCityNotFoundException("City not credentialed: " + issuingCityName));

        String rvpId = businessIdGenerator.generateRandomRVPId(targetRegion);
        RVP rvp = new RVP(trainer, targetRegion, issuingCity);
        rvp.setRvpId(rvpId);

        return rvpRepository.save(rvp);
    }
}
