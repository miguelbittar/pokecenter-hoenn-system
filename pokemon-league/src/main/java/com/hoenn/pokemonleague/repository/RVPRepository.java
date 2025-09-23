package com.hoenn.pokemonleague.repository;

import com.hoenn.pokemonleague.entity.RVP;
import com.hoenn.pokemonleague.enums.TrainerRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RVPRepository extends JpaRepository<RVP, String> {
    boolean existsByRvpIdAndIsDeletedFalse(String rvpId);

    @Query("SELECT r FROM RVP r WHERE r.trainer.trainerId = :trainerId AND r.targetRegion = :targetRegion AND r.status = 'ACTIVE' AND r.isDeleted = false")
    Optional<RVP> findActiveRVPByTrainerAndTargetRegion(@Param("trainerId") String trainerId, @Param("targetRegion") TrainerRegion targetRegion);
}
