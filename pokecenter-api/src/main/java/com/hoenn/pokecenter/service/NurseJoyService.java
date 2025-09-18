package com.hoenn.pokecenter.service;

import com.hoenn.pokecenter.entity.NurseJoy;
import com.hoenn.pokecenter.repository.NurseJoyRepository;
import com.hoenn.pokecenter.components.BusinessIdGenerator;
import com.hoenn.pokecenter.components.PasswordGenerator;
import org.springframework.stereotype.Service;

@Service
public class NurseJoyService {

    private final NurseJoyRepository nurseJoyRepository;
    private final BusinessIdGenerator businessIdGenerator;
    private final PasswordGenerator passwordGenerator;

    public NurseJoyService(NurseJoyRepository nurseJoyRepository, BusinessIdGenerator businessIdGenerator, PasswordGenerator passwordGenerator) {
        this.nurseJoyRepository = nurseJoyRepository;
        this.businessIdGenerator = businessIdGenerator;
        this.passwordGenerator = passwordGenerator;
    }

    public NurseJoy registerNurseJoy(NurseJoy nurseJoy){
        nurseJoy.setNurseJoyId(businessIdGenerator.generateSequentialNurseJoyId());
        nurseJoy.setPassword(passwordGenerator.generateTemporaryPassword());
        return nurseJoyRepository.save(nurseJoy);
    }
}
