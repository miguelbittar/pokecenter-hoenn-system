package com.hoenn.pokemonleague.service;

import com.hoenn.pokemonleague.components.BusinessIdGenerator;
import com.hoenn.pokemonleague.entity.RVP;
import com.hoenn.pokemonleague.entity.Trainer;
import com.hoenn.pokemonleague.entity.ValidCity;
import com.hoenn.pokemonleague.enums.AuthorityType;
import com.hoenn.pokemonleague.enums.TrainerRegion;
import com.hoenn.pokemonleague.exceptions.custom.DuplicateRVPException;
import com.hoenn.pokemonleague.exceptions.custom.InvalidRVPRequestException;
import com.hoenn.pokemonleague.exceptions.custom.TrainerNotFoundException;
import com.hoenn.pokemonleague.exceptions.custom.ValidCityNotFoundException;
import com.hoenn.pokemonleague.repository.RVPRepository;
import com.hoenn.pokemonleague.repository.TrainerRepository;
import com.hoenn.pokemonleague.repository.ValidCityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RVPServiceTest {

    @Mock
    private RVPRepository rvpRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private ValidCityRepository validCityRepository;

    @Mock
    private BusinessIdGenerator businessIdGenerator;

    @InjectMocks
    private RVPService rvpService;

    @Test
    @DisplayName("Should create RVP when Valid Request")
    void issueRVPCase1() {
        String trainerId = "KA123456";
        TrainerRegion targetRegion = TrainerRegion.HOENN;
        String cityName = "rustboro-city";
        AuthorityType authorityType = AuthorityType.POKECENTER;

        Trainer mockTrainer = new Trainer("Test", "test@email.com", TrainerRegion.KANTO);
        ValidCity mockCity = new ValidCity(cityName, targetRegion, authorityType);
        RVP expectedRvp = new RVP(mockTrainer, targetRegion, mockCity);
        expectedRvp.setRvpId("RVP123456-HN");

        when(trainerRepository.findByTrainerIdAndIsDeletedFalse(trainerId)).thenReturn(Optional.of(mockTrainer));
        when(rvpRepository.findActiveRVPByTrainerAndTargetRegion(trainerId, targetRegion)).thenReturn(Optional.empty());
        when(validCityRepository.findByCityNameAndRegionAndAuthorityTypeAndIsDeletedFalse(cityName, targetRegion, authorityType)).thenReturn(Optional.of(mockCity));
        when(businessIdGenerator.generateRandomRVPId(targetRegion)).thenReturn("RVP123456-HN");
        when(rvpRepository.save(any(RVP.class))).thenReturn(expectedRvp);

        RVP result = rvpService.issueRVP(trainerId, targetRegion, cityName, authorityType);

        assertThat(result).isNotNull();
        assertThat(result.getRvpId()).isEqualTo("RVP123456-HN");
        assertThat(result.getTrainer()).isEqualTo(mockTrainer);
        assertThat(result.getTargetRegion()).isEqualTo(targetRegion);
    }

    @Test
    @DisplayName("Should throw InvalidRVPRequestException when trainer requests RVP for own region")
    void issueRVPCase2() {
        String trainerId = "HN123456";
        TrainerRegion targetRegion = TrainerRegion.HOENN;
        Trainer mockTrainer = new Trainer("Test", "test@email.com", TrainerRegion.HOENN);

        when(trainerRepository.findByTrainerIdAndIsDeletedFalse(trainerId)).thenReturn(Optional.of(mockTrainer));

        assertThrows(InvalidRVPRequestException.class,
                () -> rvpService.issueRVP(trainerId, targetRegion, "any-city", AuthorityType.POKECENTER));
    }

    @Test
    @DisplayName("Should throw TrainerNotFoundException when trainer not found")
    void issueRVPCase3(){
        String trainerId = "KT123456";
        TrainerRegion targetRegion = TrainerRegion.HOENN;

        when(trainerRepository.findByTrainerIdAndIsDeletedFalse(trainerId)).thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class,
                () -> rvpService.issueRVP(trainerId, targetRegion, "petalburg-city", AuthorityType.POKECENTER));
    }

    @Test
    @DisplayName("Should throw DuplicateRVPException when trainer already has active RVP for target region")
    void issueRVPCase4(){
        String trainerId = "KT123456";
        String cityName = "petalburg-city";
        TrainerRegion targetRegion = TrainerRegion.HOENN;
        AuthorityType authorityType = AuthorityType.POKECENTER;

        Trainer mockTrainer = new Trainer("Test", "test@email.com", TrainerRegion.KANTO);
        ValidCity mockCity = new ValidCity(cityName, targetRegion, authorityType);
        RVP mockRvp = new RVP(mockTrainer, targetRegion, mockCity);

        when(trainerRepository.findByTrainerIdAndIsDeletedFalse(trainerId)).thenReturn(Optional.of(mockTrainer));
        when(rvpRepository.findActiveRVPByTrainerAndTargetRegion(trainerId, targetRegion)).thenReturn(Optional.of(mockRvp));

        assertThrows(DuplicateRVPException.class,
                () -> rvpService.issueRVP(trainerId, targetRegion, "petalburg-city", AuthorityType.POKECENTER));
    }

    @Test
    @DisplayName("Should throw ValidCityNotFoundException when city is not credentialed")
    void issueRVPCase5(){
        String trainerId = "KT123456";
        String normalizedCityName = "petalburg-city";
        TrainerRegion targetRegion = TrainerRegion.HOENN;
        AuthorityType authorityType = AuthorityType.POKECENTER;

        Trainer mockTrainer = new Trainer("Test", "test@email.com", TrainerRegion.KANTO);

        when(trainerRepository.findByTrainerIdAndIsDeletedFalse(trainerId)).thenReturn(Optional.of(mockTrainer));
        when(rvpRepository.findActiveRVPByTrainerAndTargetRegion(trainerId, targetRegion)).thenReturn(Optional.empty());
        when(validCityRepository.findByCityNameAndRegionAndAuthorityTypeAndIsDeletedFalse(normalizedCityName, targetRegion, authorityType)).thenReturn(Optional.empty());

        assertThrows(ValidCityNotFoundException.class,
                () -> rvpService.issueRVP(trainerId, targetRegion, "petalburg-city", AuthorityType.POKECENTER));
    }
}