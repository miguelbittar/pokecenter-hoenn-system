package com.hoenn.pokecenter.service;

import com.hoenn.pokecenter.entity.NurseJoy;
import com.hoenn.pokecenter.repository.NurseJoyRepository;
import com.hoenn.pokecenter.components.BusinessIdGenerator;
import com.hoenn.pokecenter.components.PasswordGenerator;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<NurseJoy> getAllNurseJoys(){
        return nurseJoyRepository.findAll();
    }

    public NurseJoy findByNurseJoyId(String nurseJoyId){
        return nurseJoyRepository.findByNurseJoyId(nurseJoyId)
                .orElseThrow(() -> new RuntimeException("No Nurse Joy found with ID: " + nurseJoyId));
    }

    public NurseJoy updateNurseJoyProfileByAdmin(String nurseJoyId, NurseJoy updateProfile){
        NurseJoy existingJoy = nurseJoyRepository.findByNurseJoyId(nurseJoyId)
                .orElseThrow(() -> new RuntimeException("No Nurse Joy found with ID: " + nurseJoyId));

        if (updateProfile.getPassword() != null){
            throw new ValidationException("Password updates not allowed through admin endpoint.");
        }

        Optional.ofNullable(updateProfile.getName()).ifPresent(existingJoy::setName);
        Optional.ofNullable(updateProfile.getEmail()).ifPresent(existingJoy::setEmail);
        Optional.ofNullable(updateProfile.getCity()).ifPresent(existingJoy::setCity);
        Optional.ofNullable(updateProfile.getRegion()).ifPresent(existingJoy::setRegion);
        Optional.ofNullable(updateProfile.getRole()).ifPresent(existingJoy::setRole);

        return nurseJoyRepository.save(existingJoy);
    }

    public NurseJoy updateOwnProfile(String nurseJoyId, NurseJoy updateProfile){
        NurseJoy existingJoy = nurseJoyRepository.findByNurseJoyId(nurseJoyId)
                .orElseThrow(() -> new RuntimeException("No Nurse Joy found with ID: " + nurseJoyId));

        if (updateProfile.getRole() != null){
            throw new ValidationException("Role updates not allowed through common endpoint.");
        }

        Optional.ofNullable(updateProfile.getName()).ifPresent(existingJoy::setName);
        Optional.ofNullable(updateProfile.getEmail()).ifPresent(existingJoy::setEmail);
        Optional.ofNullable(updateProfile.getPassword()).ifPresent(existingJoy::setPassword);
        Optional.ofNullable(updateProfile.getCity()).ifPresent(existingJoy::setCity);
        Optional.ofNullable(updateProfile.getRegion()).ifPresent(existingJoy::setRegion);

        return nurseJoyRepository.save(existingJoy);
    }

    @Transactional
    public void deleteByNurseJoyId(String nurseJoyId){
        NurseJoy existingJoy = nurseJoyRepository.findByNurseJoyId(nurseJoyId)
                .orElseThrow(() -> new RuntimeException("No Nurse Joy found with ID: " + nurseJoyId));
        nurseJoyRepository.deleteByNurseJoyId(nurseJoyId);
    }
}
