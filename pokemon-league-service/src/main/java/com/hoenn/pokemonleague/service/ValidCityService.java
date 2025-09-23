package com.hoenn.pokemonleague.service;

import com.hoenn.pokemonleague.entity.ValidCity;
import com.hoenn.pokemonleague.enums.AuthorityType;
import com.hoenn.pokemonleague.enums.TrainerRegion;
import com.hoenn.pokemonleague.exceptions.custom.ValidCityAlreadyExistsException;
import com.hoenn.pokemonleague.repository.ValidCityRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ValidCityService {

    private final ValidCityRepository validCityRepository;

    public ValidCityService(ValidCityRepository validCityRepository) {
        this.validCityRepository = validCityRepository;
    }

    public ValidCity registerValidCity(ValidCity validCity) {

        String normalizedCityName = normalizeCityName(validCity.getCityName());
        validCity.setCityName(normalizedCityName);

        validateUniqueCityAuthorityRegion(
                validCity.getCityName(),
                validCity.getRegion(),
                validCity.getAuthorityType()
        );

        String authorityName = generateAuthorityName(
                validCity.getCityName(),
                validCity.getRegion(),
                validCity.getAuthorityType()
        );
        validCity.setAuthorityName(authorityName);

        return validCityRepository.save(validCity);
    }

    public List<ValidCity> getAllValidCities() {
        return validCityRepository.findByIsDeletedFalse();
    }

    private void validateUniqueCityAuthorityRegion(String cityName, TrainerRegion region, AuthorityType authorityType) {
        boolean exists = validCityRepository.existsByCityNameAndRegionAndAuthorityTypeAndIsDeletedFalse(
                cityName, region, authorityType
        );

        if (exists) {
            throw new ValidCityAlreadyExistsException(
                    "Valid city already exists: " + cityName + " (" + authorityType.getDisplayName() + ")"
            );
        }
    }

    private String generateAuthorityName(String cityName, TrainerRegion region, AuthorityType authorityType) {
        String formattedCityName = formatCityName(cityName);

        return switch (authorityType) {
            case POKECENTER -> formattedCityName + " PokéCenter - " + region.name();
            case GYM -> formattedCityName + " Gym - " + region.name();
            case POKEMON_LEAGUE -> region.name() + " Pokemon League - " + region.name();
        };
    }

    private String normalizeCityName(String cityName) {
        return cityName.trim()
                .toLowerCase()
                .replaceAll("\\s+", "-")
                .replaceAll("_", "-");
    }

    private String formatCityName(String cityName) {
        return Arrays.stream(cityName.trim()
                        .toLowerCase()
                        .replaceAll("[_\\-]+", " ")
                        .split("\\s+"))
                .filter(s -> !s.isEmpty())
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                .collect(Collectors.joining(" "));
    }
}