package com.hoenn.pokecenter.repository;

import com.hoenn.pokecenter.entity.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PokemonRepository extends JpaRepository<Pokemon, String> {
    Optional<Pokemon> findTopByIsDeletedFalseOrderByCreatedAtDesc();
}
