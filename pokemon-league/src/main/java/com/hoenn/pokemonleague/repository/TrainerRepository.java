package com.hoenn.pokemonleague.repository;

import com.hoenn.pokemonleague.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer, String> {
    boolean existsByTrainerIdAndIsDeletedFalse(String trainerId);
}