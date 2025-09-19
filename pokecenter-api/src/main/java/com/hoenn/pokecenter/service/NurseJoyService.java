package com.hoenn.pokecenter.service;

import com.hoenn.pokecenter.entity.NurseJoy;
import com.hoenn.pokecenter.exception.custom.EmailAlreadyExistsException;
import com.hoenn.pokecenter.exception.custom.NurseJoyNotFoundException;
import com.hoenn.pokecenter.exception.custom.PasswordChangeNotAllowedException;
import com.hoenn.pokecenter.exception.custom.UnauthorizedRoleChangeException;
import com.hoenn.pokecenter.repository.NurseJoyRepository;
import com.hoenn.pokecenter.components.BusinessIdGenerator;
import com.hoenn.pokecenter.components.PasswordGenerator;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NurseJoyService {

    private final NurseJoyRepository nurseJoyRepository;
    private final PasswordEncoder passwordEncoder;
    private final BusinessIdGenerator businessIdGenerator;
    private final PasswordGenerator passwordGenerator;

    public NurseJoyService(NurseJoyRepository nurseJoyRepository, PasswordEncoder passwordEncoder, BusinessIdGenerator businessIdGenerator, PasswordGenerator passwordGenerator) {
        this.nurseJoyRepository = nurseJoyRepository;
        this.passwordEncoder = passwordEncoder;
        this.businessIdGenerator = businessIdGenerator;
        this.passwordGenerator = passwordGenerator;
    }

    public NurseJoy registerNurseJoy(NurseJoy nurseJoy){

        if (nurseJoyRepository.findByEmailAndIsDeletedFalse(nurseJoy.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        nurseJoy.setNurseJoyId(businessIdGenerator.generateSequentialNurseJoyId());

        String temporaryPassword = passwordGenerator.generateTemporaryPassword();
        String encodedPassword = passwordEncoder.encode(temporaryPassword);
        nurseJoy.setPassword(encodedPassword);

        return nurseJoyRepository.save(nurseJoy);
    }

    public List<NurseJoy> getAllNurseJoys(){
        return nurseJoyRepository.findByIsDeletedFalse();
    }

    public NurseJoy findByNurseJoyId(String nurseJoyId){
        return nurseJoyRepository.findByNurseJoyIdAndIsDeletedFalse(nurseJoyId)
                .orElseThrow(() -> new NurseJoyNotFoundException("No Nurse Joy found with ID: " + nurseJoyId));
    }

    public NurseJoy updateNurseJoyProfileByAdmin(String nurseJoyId, NurseJoy updateProfile){
        NurseJoy existingJoy = findByNurseJoyId(nurseJoyId);

        if (updateProfile.getPassword() != null){
            throw new PasswordChangeNotAllowedException("Password updates not allowed through admin endpoint");
        }

        if (updateProfile.getEmail() != null) {
            checkEmailExists(updateProfile.getEmail(), nurseJoyId);
        }

        Optional.ofNullable(updateProfile.getName()).ifPresent(existingJoy::setName);
        Optional.ofNullable(updateProfile.getEmail()).ifPresent(existingJoy::setEmail);
        Optional.ofNullable(updateProfile.getCity()).ifPresent(existingJoy::setCity);
        Optional.ofNullable(updateProfile.getRegion()).ifPresent(existingJoy::setRegion);
        Optional.ofNullable(updateProfile.getRole()).ifPresent(existingJoy::setRole);

        return nurseJoyRepository.save(existingJoy);
    }

    public NurseJoy updateOwnProfile(String nurseJoyId, NurseJoy updateProfile){
        NurseJoy existingJoy = findByNurseJoyId(nurseJoyId);

        if (updateProfile.getRole() != null){
            throw new UnauthorizedRoleChangeException("Role updates not allowed through common endpoint");
        }

        if (updateProfile.getEmail() != null) {
            checkEmailExists(updateProfile.getEmail(), nurseJoyId);
        }

        Optional.ofNullable(updateProfile.getName()).ifPresent(existingJoy::setName);
        Optional.ofNullable(updateProfile.getEmail()).ifPresent(existingJoy::setEmail);
        Optional.ofNullable(updateProfile.getCity()).ifPresent(existingJoy::setCity);
        Optional.ofNullable(updateProfile.getRegion()).ifPresent(existingJoy::setRegion);

        Optional.ofNullable(updateProfile.getPassword())
                .ifPresent(password -> existingJoy.setPassword(passwordEncoder.encode(password)));

        return nurseJoyRepository.save(existingJoy);
    }

    @Transactional
    public void deleteByNurseJoyId(String nurseJoyId){
        NurseJoy existingJoy = findByNurseJoyId(nurseJoyId);
        existingJoy.softDelete();
        nurseJoyRepository.save(existingJoy);
    }

    private void checkEmailExists(String email, String nurseJoyId) {
        Optional<NurseJoy> existingJoy = nurseJoyRepository.findByEmailAndIsDeletedFalse(email);
        if (existingJoy.isPresent() && !existingJoy.get().getNurseJoyId().equals(nurseJoyId)) {
            throw new EmailAlreadyExistsException("Email already in use");
        }
    }
}
