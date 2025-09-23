package com.hoenn.pokemonleague.service;

import com.hoenn.pokemonleague.components.BusinessIdGenerator;
import com.hoenn.pokemonleague.dto.response.RVPStatusResponse;
import com.hoenn.pokemonleague.entity.RVP;
import com.hoenn.pokemonleague.entity.Trainer;
import com.hoenn.pokemonleague.entity.ValidCity;
import com.hoenn.pokemonleague.enums.RVPStatus;
import com.hoenn.pokemonleague.enums.TrainerRegion;
import com.hoenn.pokemonleague.exceptions.custom.DuplicateRVPException;
import com.hoenn.pokemonleague.exceptions.custom.InvalidRVPRequestException;
import com.hoenn.pokemonleague.exceptions.custom.TrainerNotFoundException;
import com.hoenn.pokemonleague.exceptions.custom.ValidCityNotFoundException;
import com.hoenn.pokemonleague.mapper.RVPMapper;
import com.hoenn.pokemonleague.repository.RVPRepository;
import com.hoenn.pokemonleague.repository.TrainerRepository;
import com.hoenn.pokemonleague.repository.ValidCityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

        String normalizedCityName = normalizeCityName(issuingCityName);
        ValidCity issuingCity = validCityRepository.findByCityNameAndIsDeletedFalse(normalizedCityName)
                .orElseThrow(() -> new ValidCityNotFoundException("City not credentialed: " + issuingCityName));

        String rvpId = businessIdGenerator.generateRandomRVPId(targetRegion);
        RVP rvp = new RVP(trainer, targetRegion, issuingCity);
        rvp.setRvpId(rvpId);

        return rvpRepository.save(rvp);
    }

    public RVPStatusResponse validateRVPStatus(String trainerId, TrainerRegion targetRegion) {
        Optional<RVP> rvpOpt = rvpRepository.findActiveRVPByTrainerAndTargetRegion(trainerId, targetRegion);

        if (rvpOpt.isEmpty()) {
            return RVPMapper.toNoRVPResponse();
        }

        RVP rvp = rvpOpt.get();

        if (rvp.getExpiryDate().isBefore(LocalDate.now())) {
            if (rvp.getStatus() == RVPStatus.ACTIVE) {
                rvp.setStatus(RVPStatus.EXPIRED);
                rvpRepository.save(rvp);
            }
            return new RVPStatusResponse(
                    false,
                    "EXPIRED",
                    rvp.getExpiryDate().toString(),
                    "RVP has expired"
            );
        }

        if (rvp.getStatus() == RVPStatus.REVOKED) {
            return new RVPStatusResponse(
                    false,
                    "REVOKED",
                    rvp.getExpiryDate().toString(),
                    "RVP has been revoked"
            );
        }

        return RVPMapper.toStatusResponse(rvp);
    }

    private String normalizeCityName(String cityName) {
        return cityName.trim()
                .toLowerCase()
                .replaceAll("\\s+", "-")
                .replaceAll("_", "-");
    }
}
