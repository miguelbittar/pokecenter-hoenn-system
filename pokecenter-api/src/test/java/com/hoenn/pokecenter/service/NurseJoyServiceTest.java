package com.hoenn.pokecenter.service;

import com.hoenn.pokecenter.components.BusinessIdGenerator;
import com.hoenn.pokecenter.components.PasswordGenerator;
import com.hoenn.pokecenter.entity.NurseJoy;
import com.hoenn.pokecenter.enums.NurseJoyRole;
import com.hoenn.pokecenter.exception.custom.EmailAlreadyExistsException;
import com.hoenn.pokecenter.exception.custom.NurseJoyNotFoundException;
import com.hoenn.pokecenter.exception.custom.PasswordChangeNotAllowedException;
import com.hoenn.pokecenter.repository.NurseJoyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NurseJoyServiceTest {

    @Mock
    private NurseJoyRepository nurseJoyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private BusinessIdGenerator businessIdGenerator;

    @Mock
    private PasswordGenerator passwordGenerator;

    @InjectMocks
    private NurseJoyService nurseJoyService;

    @Test
    @DisplayName("Should register Nurse Joy with generated ID and encoded password")
    void registerNurseJoyCase1() {
        NurseJoy nurseJoy = createTestNurseJoy();

        when(nurseJoyRepository.findByEmailAndIsDeletedFalse(nurseJoy.getEmail())).thenReturn(Optional.empty());
        when(businessIdGenerator.generateSequentialNurseJoyId()).thenReturn("JOY000001");
        when(passwordGenerator.generateTemporaryPassword()).thenReturn("ABCDabcd8");
        when(passwordEncoder.encode("ABCDabcd8")).thenReturn("encoded_password");
        when(nurseJoyRepository.save(any(NurseJoy.class))).thenAnswer(invocation -> invocation.getArgument(0));

        NurseJoy result = nurseJoyService.registerNurseJoy(nurseJoy);

        assertThat(result.getNurseJoyId()).isEqualTo("JOY000001");
        assertThat(result.getPassword()).isEqualTo("encoded_password");

        verify(passwordEncoder).encode("ABCDabcd8");
        verify(nurseJoyRepository).save(any(NurseJoy.class));
    }

    @Test
    @DisplayName("Should throw EmailAlreadyExistsException when email already exists")
    void registerNurseJoyCase2() {
        NurseJoy nurseJoy = createTestNurseJoy();

        when(nurseJoyRepository.findByEmailAndIsDeletedFalse(nurseJoy.getEmail())).thenReturn(Optional.of(nurseJoy));

        assertThrows(EmailAlreadyExistsException.class,
                () -> nurseJoyService.registerNurseJoy(nurseJoy));

        verify(nurseJoyRepository).findByEmailAndIsDeletedFalse(nurseJoy.getEmail());
    }

    @Test
    @DisplayName("Should return Nurse Joy when ID exists")
    void findByNurseJoyIdCase1() {
        NurseJoy nurseJoy = createTestNurseJoy();
        nurseJoy.setNurseJoyId("JOY000001");

        when(nurseJoyRepository.findByNurseJoyIdAndIsDeletedFalse(nurseJoy.getNurseJoyId())).thenReturn(Optional.of(nurseJoy));

        NurseJoy result = nurseJoyService.findByNurseJoyId(nurseJoy.getNurseJoyId());

        assertThat(result).isEqualTo(nurseJoy);
        verify(nurseJoyRepository).findByNurseJoyIdAndIsDeletedFalse(nurseJoy.getNurseJoyId());
    }

    @Test
    @DisplayName("Should throw NurseJoyNotFoundException when ID not found")
    void findByNurseJoyIdCase2(){

        String joyId = "JOY000001";
        when(nurseJoyRepository.findByNurseJoyIdAndIsDeletedFalse(joyId)).thenReturn(Optional.empty());

        assertThrows(NurseJoyNotFoundException.class,
                () -> nurseJoyService.findByNurseJoyId(joyId));

        verify(nurseJoyRepository).findByNurseJoyIdAndIsDeletedFalse(joyId);
    }

    @Test
    @DisplayName("Should update Nurse Joy profile successfully by admin")
    void updateNurseJoyProfileByAdminCase1() {
        NurseJoy existingJoy = createTestNurseJoy();
        existingJoy.setNurseJoyId("JOY000001");
        existingJoy.setEmail("old@email.com");

        NurseJoy updateProfile = createTestNurseJoy();
        updateProfile.setName("Updated Name");
        updateProfile.setEmail("new@email.com");
        updateProfile.setCity("New City");

        when(nurseJoyRepository.findByNurseJoyIdAndIsDeletedFalse("JOY000001")).thenReturn(Optional.of(existingJoy));
        when(nurseJoyRepository.findByEmailAndIsDeletedFalse("new@email.com")).thenReturn(Optional.empty());
        when(nurseJoyRepository.save(any(NurseJoy.class))).thenAnswer(invocation -> invocation.getArgument(0));

        NurseJoy result = nurseJoyService.updateNurseJoyProfileByAdmin("JOY000001", updateProfile);

        assertThat(result.getName()).isEqualTo("Updated Name");
        assertThat(result.getEmail()).isEqualTo("new@email.com");
        assertThat(result.getCity()).isEqualTo("New City");

        verify(nurseJoyRepository).findByEmailAndIsDeletedFalse("new@email.com");
        verify(nurseJoyRepository).save(any(NurseJoy.class));
    }

    @Test
    @DisplayName("Should throw PasswordChangeNotAllowedException when trying to update password")
    void updateNurseJoyProfileByAdminCase2() {
        NurseJoy existingJoy = createTestNurseJoy();
        existingJoy.setNurseJoyId("JOY000001");

        NurseJoy updateProfile = createTestNurseJoy();
        updateProfile.setName("Updated Name");
        updateProfile.setPassword("newPassword123");

        when(nurseJoyRepository.findByNurseJoyIdAndIsDeletedFalse("JOY000001")).thenReturn(Optional.of(existingJoy));

        assertThrows(PasswordChangeNotAllowedException.class,
                () -> nurseJoyService.updateNurseJoyProfileByAdmin("JOY000001", updateProfile));

        verify(nurseJoyRepository).findByNurseJoyIdAndIsDeletedFalse("JOY000001");
    }

    @Test
    @DisplayName("Should throw EmailAlreadyExistsException when email already in use by another Joy")
    void updateNurseJoyProfileByAdminCase3() {
        NurseJoy existingJoy = createTestNurseJoy();
        existingJoy.setNurseJoyId("JOY000001");

        NurseJoy anotherJoy = createTestNurseJoy();
        anotherJoy.setNurseJoyId("JOY000002");
        anotherJoy.setEmail("taken@email.com");

        NurseJoy updateProfile = createTestNurseJoy();
        updateProfile.setEmail("taken@email.com");

        when(nurseJoyRepository.findByNurseJoyIdAndIsDeletedFalse("JOY000001")).thenReturn(Optional.of(existingJoy));
        when(nurseJoyRepository.findByEmailAndIsDeletedFalse("taken@email.com")).thenReturn(Optional.of(anotherJoy));

        assertThrows(EmailAlreadyExistsException.class,
                () -> nurseJoyService.updateNurseJoyProfileByAdmin("JOY000001", updateProfile));

        verify(nurseJoyRepository).findByEmailAndIsDeletedFalse("taken@email.com");
    }

    @Test
    @DisplayName("Should soft delete Nurse Joy successfully")
    void deleteByNurseJoyIdCase1() {
        NurseJoy existingJoy = createTestNurseJoy();
        existingJoy.setNurseJoyId("JOY000001");

        when(nurseJoyRepository.findByNurseJoyIdAndIsDeletedFalse("JOY000001")).thenReturn(Optional.of(existingJoy));
        when(nurseJoyRepository.save(any(NurseJoy.class))).thenAnswer(invocation -> invocation.getArgument(0));

        nurseJoyService.deleteByNurseJoyId("JOY000001");

        assertThat(existingJoy.getIsDeleted()).isTrue();
        assertThat(existingJoy.getDeletedAt()).isNotNull();
        verify(nurseJoyRepository).findByNurseJoyIdAndIsDeletedFalse("JOY000001");
        verify(nurseJoyRepository).save(existingJoy);
    }

    private NurseJoy createTestNurseJoy(){
        NurseJoy nurseJoy = new NurseJoy();
        nurseJoy.setName("Amanda");
        nurseJoy.setEmail("amanda@test.com");
        nurseJoy.setCity("Rustboro City");
        nurseJoy.setRegion("HOENN");
        nurseJoy.setRole(NurseJoyRole.COMMON);
        return nurseJoy;
    }
}