package com.hoenn.pokemonleague.repository;

import com.hoenn.pokemonleague.entity.ValidCity;
import com.hoenn.pokemonleague.enums.AuthorityType;
import com.hoenn.pokemonleague.enums.TrainerRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ValidCityRepository extends JpaRepository<ValidCity, String> {

    Optional<ValidCity> findByCityNameAndRegionAndAuthorityTypeAndIsDeletedFalse(
            String cityName,
            TrainerRegion region,
            AuthorityType authorityType
    );

    List<ValidCity> findByIsDeletedFalse();

    boolean existsByCityNameAndRegionAndAuthorityTypeAndIsDeletedFalse(
            String cityName,
            TrainerRegion region,
            AuthorityType authorityType
    );
}
