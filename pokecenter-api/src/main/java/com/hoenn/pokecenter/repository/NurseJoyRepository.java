package com.hoenn.pokecenter.repository;

import com.hoenn.pokecenter.entity.NurseJoy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NurseJoyRepository extends JpaRepository<NurseJoy, String> {
    Optional<NurseJoy> findTopByOrderByCreatedAtDesc();
    Optional<NurseJoy> findByNurseJoyId(String nurseJoyId);
    void deleteByNurseJoyId(String nurseJoyId);
    Optional<NurseJoy> findByEmail(String email);
}
