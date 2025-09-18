package com.hoenn.pokecenter.service;

import com.hoenn.pokecenter.entity.NurseJoy;
import com.hoenn.pokecenter.repository.NurseJoyRepository;
import com.hoenn.pokecenter.utils.BusinessIdGenerator;
import org.springframework.stereotype.Service;

@Service
public class NurseJoyService {

    private final NurseJoyRepository nurseJoyRepository;
    private final BusinessIdGenerator businessIdGenerator;

    public NurseJoyService(NurseJoyRepository nurseJoyRepository, BusinessIdGenerator businessIdGenerator) {
        this.nurseJoyRepository = nurseJoyRepository;
        this.businessIdGenerator = businessIdGenerator;
    }

    public NurseJoy registerNurseJoy(NurseJoy nurseJoy){
        nurseJoy.setNurseJoyId(businessIdGenerator.generateSequentialNurseJoyId());
        //TODO: PASSWORD GENERATOR
        nurseJoy.setPassword("12345678"); // only for test...
        return nurseJoyRepository.save(nurseJoy);
    }
}
