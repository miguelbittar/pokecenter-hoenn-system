package com.hoenn.pokemonleague.repository;

import com.hoenn.pokemonleague.entity.RVP;
import com.hoenn.pokemonleague.enums.TrainerRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RVPRepository extends JpaRepository<RVP, String> {
    boolean existsByRvpIdAndIsDeletedFalse(String rvpId);
    Optional<RVP> findActiveRVPByTrainerAndTargetRegion(String trainerId, TrainerRegion targetRegion);
}
