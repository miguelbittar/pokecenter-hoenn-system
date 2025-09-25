package com.hoenn.pokemonleague.service;

import com.hoenn.pokemonleague.entity.ValidCity;
import com.hoenn.pokemonleague.enums.AuthorityType;
import com.hoenn.pokemonleague.enums.TrainerRegion;
import com.hoenn.pokemonleague.exceptions.custom.ValidCityAlreadyExistsException;
import com.hoenn.pokemonleague.repository.ValidCityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidCityServiceTest {

    @Mock
    private ValidCityRepository validCityRepository;

    @InjectMocks
    private ValidCityService validCityService;

    @Test
    @DisplayName("Should normalize city name, generate authority name and save valid city successfully")
    void registerValidCityCase1() {
        ValidCity inputCity = new ValidCity("  RUSTBORO CITY  ", TrainerRegion.HOENN, AuthorityType.POKECENTER);

        when(validCityRepository.existsByCityNameAndRegionAndAuthorityTypeAndIsDeletedFalse("rustboro-city", TrainerRegion.HOENN, AuthorityType.POKECENTER)).thenReturn(false);
        when(validCityRepository.save(any(ValidCity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ValidCity result = validCityService.registerValidCity(inputCity);

        assertThat(result.getCityName()).isEqualTo("rustboro-city");
        assertThat(result.getAuthorityName()).contains("Rustboro City PokéCenter");

        verify(validCityRepository).existsByCityNameAndRegionAndAuthorityTypeAndIsDeletedFalse("rustboro-city", TrainerRegion.HOENN, AuthorityType.POKECENTER);
        verify(validCityRepository).save(any(ValidCity.class));
    }

    @Test
    @DisplayName("Should throw ValidCityAlreadyExistsException when city already exists")
    void registerValidCityCase2(){
        ValidCity inputCity = new ValidCity("  RUSTBORO CITY  ", TrainerRegion.HOENN, AuthorityType.POKECENTER);

        when(validCityRepository.existsByCityNameAndRegionAndAuthorityTypeAndIsDeletedFalse("rustboro-city", TrainerRegion.HOENN, AuthorityType.POKECENTER)).thenReturn(true);

        assertThrows(ValidCityAlreadyExistsException.class,
                () -> validCityService.registerValidCity(inputCity));

        verify(validCityRepository).existsByCityNameAndRegionAndAuthorityTypeAndIsDeletedFalse("rustboro-city", TrainerRegion.HOENN, AuthorityType.POKECENTER);
    }
}