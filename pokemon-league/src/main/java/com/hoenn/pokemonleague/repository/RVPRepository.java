package com.hoenn.pokemonleague.repository;

import com.hoenn.pokemonleague.entity.RVP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RVPRepository extends JpaRepository<RVP, String> {
    boolean existsByRvpIdAndIsDeletedFalse(String rvpId);
}
