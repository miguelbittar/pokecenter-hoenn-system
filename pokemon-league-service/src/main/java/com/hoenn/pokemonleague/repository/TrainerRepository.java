package com.hoenn.pokemonleague.repository;

import com.hoenn.pokemonleague.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, String> {
    boolean existsByTrainerIdAndIsDeletedFalse(String trainerId);
    Optional<Trainer> findByTrainerIdAndIsDeletedFalse(String trainerId);
    List<Trainer> findByIsDeletedFalse();
}