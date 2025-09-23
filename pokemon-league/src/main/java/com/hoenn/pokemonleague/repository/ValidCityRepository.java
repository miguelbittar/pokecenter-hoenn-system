package com.hoenn.pokemonleague.repository;

import com.hoenn.pokemonleague.entity.ValidCity;
import com.hoenn.pokemonleague.enums.TrainerRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ValidCityRepository extends JpaRepository<ValidCity, String> {
    Optional<ValidCity> findByCityNameAndIsDeletedFalse(String cityName);
}
