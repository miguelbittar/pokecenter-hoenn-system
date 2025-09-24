package com.hoenn.pokemonleague.service;

import com.hoenn.pokemonleague.components.BusinessIdGenerator;
import com.hoenn.pokemonleague.dto.response.RVPStatusResponse;
import com.hoenn.pokemonleague.entity.RVP;
import com.hoenn.pokemonleague.entity.Trainer;
import com.hoenn.pokemonleague.entity.ValidCity;
import com.hoenn.pokemonleague.enums.AuthorityType;
import com.hoenn.pokemonleague.enums.RVPStatus;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
        String cityName = " PETALBURG CITY ";
        TrainerRegion targetRegion = TrainerRegion.HOENN;
        AuthorityType authorityType = AuthorityType.POKECENTER;

        Trainer mockTrainer = new Trainer("Test", "test@email.com", TrainerRegion.KANTO);

        when(trainerRepository.findByTrainerIdAndIsDeletedFalse(trainerId)).thenReturn(Optional.of(mockTrainer));
        when(rvpRepository.findActiveRVPByTrainerAndTargetRegion(trainerId, targetRegion)).thenReturn(Optional.empty());
        when(validCityRepository.findByCityNameAndRegionAndAuthorityTypeAndIsDeletedFalse("petalburg-city", targetRegion, authorityType)).thenReturn(Optional.empty());

        assertThrows(ValidCityNotFoundException.class,
                () -> rvpService.issueRVP(trainerId, targetRegion, "petalburg-city", AuthorityType.POKECENTER));

        verify(validCityRepository).findByCityNameAndRegionAndAuthorityTypeAndIsDeletedFalse("petalburg-city", targetRegion, authorityType);
    }

    @Test
    @DisplayName("Should return no RVP response when no active RVP exists")
    void validateRVPStatusCase1(){
        String trainerId = "KT123456";
        TrainerRegion targetRegion = TrainerRegion.HOENN;

        when(rvpRepository.findActiveRVPByTrainerAndTargetRegion(trainerId, targetRegion)).thenReturn(Optional.empty());

        RVPStatusResponse response = rvpService.validateRVPStatus(trainerId, targetRegion);

        assertThat(response.hasValidRVP()).isFalse();
        assertThat(response.status()).isNull();
        assertThat(response.expiryDate()).isNull();
        assertThat(response.message()).isEqualTo("No valid RVP found for target region");
    }

    @Test
    @DisplayName("Should update status and return expired when RVP is expired")
    void validateRVPStatusCase2 (){

        String trainerId = "KT123456";
        TrainerRegion targetRegion = TrainerRegion.HOENN;

        Trainer trainer = new Trainer("Test", "test@email.com", TrainerRegion.KANTO);
        ValidCity city = new ValidCity("petalburg-city", targetRegion, AuthorityType.POKECENTER);
        RVP mockRvp = new RVP(trainer, targetRegion, city);
        mockRvp.setRvpId("RVP123456-HN");
        mockRvp.setExpiryDate(LocalDate.now().minusDays(1));
        mockRvp.setStatus(RVPStatus.ACTIVE);

        when(rvpRepository.findActiveRVPByTrainerAndTargetRegion(trainerId, targetRegion)).thenReturn(Optional.of(mockRvp));
        when(rvpRepository.save(mockRvp)).thenReturn(mockRvp);

        RVPStatusResponse response = rvpService.validateRVPStatus(trainerId, targetRegion);

        assertThat(response.hasValidRVP()).isFalse();
        assertThat(response.status()).isEqualTo("EXPIRED");
        assertThat(response.expiryDate()).isEqualTo(mockRvp.getExpiryDate().toString());
        assertThat(response.message()).isEqualTo("RVP has expired");
        assertThat(mockRvp.getStatus()).isEqualTo(RVPStatus.EXPIRED);

        verify(rvpRepository).save(mockRvp);
    }

    @Test
    @DisplayName("Should return revoked status when RVP is revoked")
    void validateRVPStatusCase3() {
        String trainerId = "KT123456";
        TrainerRegion targetRegion = TrainerRegion.HOENN;

        Trainer trainer = new Trainer("Test", "test@email.com", TrainerRegion.KANTO);
        ValidCity city = new ValidCity("petalburg-city", targetRegion, AuthorityType.POKECENTER);

        RVP mockRvp = new RVP(trainer, targetRegion, city);
        mockRvp.setRvpId("RVP123456-HN");
        mockRvp.setStatus(RVPStatus.REVOKED);
        mockRvp.setExpiryDate(LocalDate.now().plusDays(10));

        when(rvpRepository.findActiveRVPByTrainerAndTargetRegion(trainerId, targetRegion)).thenReturn(Optional.of(mockRvp));

        RVPStatusResponse response = rvpService.validateRVPStatus(trainerId, targetRegion);

        assertThat(response.hasValidRVP()).isFalse();
        assertThat(response.status()).isEqualTo("REVOKED");
        assertThat(response.expiryDate()).isEqualTo(mockRvp.getExpiryDate().toString());
        assertThat(response.message()).isEqualTo("RVP has been revoked");
        assertThat(mockRvp.getStatus()).isEqualTo(RVPStatus.REVOKED);

        verify(rvpRepository).findActiveRVPByTrainerAndTargetRegion(trainerId, targetRegion);
    }

    @Test
    @DisplayName("Should return active status when RVP is valid")
    void validateRVPStatusCase4() {
        String trainerId = "KT123456";
        TrainerRegion targetRegion = TrainerRegion.HOENN;

        Trainer trainer = new Trainer("Test", "test@email.com", TrainerRegion.KANTO);
        ValidCity city = new ValidCity("petalburg-city", targetRegion, AuthorityType.POKECENTER);

        RVP mockRvp = new RVP(trainer, targetRegion, city);
        mockRvp.setRvpId("RVP123456-HN");
        mockRvp.setStatus(RVPStatus.ACTIVE);
        mockRvp.setExpiryDate(LocalDate.now().plusDays(10));

        when(rvpRepository.findActiveRVPByTrainerAndTargetRegion(trainerId, targetRegion))
                .thenReturn(Optional.of(mockRvp));

        RVPStatusResponse response = rvpService.validateRVPStatus(trainerId, targetRegion);

        assertThat(response.hasValidRVP()).isTrue();
        assertThat(response.status()).isEqualTo("ACTIVE");
        assertThat(response.expiryDate()).isEqualTo(mockRvp.getExpiryDate().toString());
        assertThat(response.message()).isEqualTo("Valid RVP found");

        verify(rvpRepository).findActiveRVPByTrainerAndTargetRegion(trainerId, targetRegion);
    }
}