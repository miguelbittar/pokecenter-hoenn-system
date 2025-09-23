package com.hoenn.pokemonleague.mapper;

import com.hoenn.pokemonleague.dto.request.ValidCityRequest;
import com.hoenn.pokemonleague.dto.response.ValidCityResponse;
import com.hoenn.pokemonleague.entity.ValidCity;

public final class ValidCityMapper {

    private ValidCityMapper() {}

    public static ValidCity toEntity(ValidCityRequest request) {
        return new ValidCity(
                request.cityName(),
                request.region(),
                request.authorityType()
        );
    }

    public static ValidCityResponse toResponse(ValidCity validCity) {
        return new ValidCityResponse(
                validCity.getId(),
                validCity.getCityName(),
                validCity.getRegion(),
                validCity.getAuthorityType(),
                validCity.getAuthorityName(),
                validCity.getCreatedAt()
        );
    }
}
