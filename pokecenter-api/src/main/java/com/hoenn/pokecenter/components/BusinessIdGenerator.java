package com.hoenn.pokecenter.components;

import com.hoenn.pokecenter.entity.NurseJoy;
import com.hoenn.pokecenter.repository.NurseJoyRepository;
import org.springframework.stereotype.Component;

@Component
public class BusinessIdGenerator {

    private final NurseJoyRepository nurseJoyRepository;

    public BusinessIdGenerator(NurseJoyRepository nurseJoyRepository) {
        this.nurseJoyRepository = nurseJoyRepository;
    }

    public String generateSequentialNurseJoyId() {
        String lastNurseJoyId = nurseJoyRepository.findTopByIsDeletedFalseOrderByCreatedAtDesc()
                .map(NurseJoy::getNurseJoyId)
                .orElse("JOY000000");
        int nextNumber = Integer.parseInt(lastNurseJoyId.substring(3)) + 1;
        return "JOY" + String.format("%06d", nextNumber);
    }
}
