package com.hoenn.pokecenter.service;

import com.hoenn.pokecenter.dto.request.LoginRequest;
import com.hoenn.pokecenter.dto.response.LoginResponse;
import com.hoenn.pokecenter.entity.NurseJoy;
import com.hoenn.pokecenter.enums.NurseJoyRole;
import com.hoenn.pokecenter.exception.custom.InvalidCredentialsException;
import com.hoenn.pokecenter.repository.NurseJoyRepository;
import com.hoenn.pokecenter.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private NurseJoyRepository nurseJoyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Should login successfully with valid credentials")
    void loginCase1() {
        LoginRequest request = new LoginRequest("JOY000001", "password123");
        NurseJoy nurseJoy = createTestNurseJoy();
        nurseJoy.setNurseJoyId("JOY000001");
        nurseJoy.setPassword("encoded_password");
        nurseJoy.setRole(NurseJoyRole.COMMON);

        when(nurseJoyRepository.findByNurseJoyIdAndIsDeletedFalse("JOY000001")).thenReturn(Optional.of(nurseJoy));
        when(passwordEncoder.matches("password123", "encoded_password")).thenReturn(true);
        when(jwtService.generateToken(nurseJoy)).thenReturn("jwt_token_123");

        LoginResponse result = authService.login(request);

        assertThat(result.token()).isEqualTo("jwt_token_123");
        assertThat(result.nurseJoyId()).isEqualTo("JOY000001");
        assertThat(result.role()).isEqualTo(NurseJoyRole.COMMON);

        verify(passwordEncoder).matches("password123", "encoded_password");
        verify(jwtService).generateToken(nurseJoy);
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when user not found")
    void loginCase2() {
        LoginRequest request = new LoginRequest("JOY000001", "password123");

        when(nurseJoyRepository.findByNurseJoyIdAndIsDeletedFalse("JOY000001")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(request));

        verify(nurseJoyRepository).findByNurseJoyIdAndIsDeletedFalse("JOY000001");
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when password incorrect")
    void loginCase3() {
        LoginRequest request = new LoginRequest("JOY000001", "wrong_password");
        NurseJoy nurseJoy = createTestNurseJoy();
        nurseJoy.setNurseJoyId("JOY000001");
        nurseJoy.setPassword("encoded_password");

        when(nurseJoyRepository.findByNurseJoyIdAndIsDeletedFalse("JOY000001")).thenReturn(Optional.of(nurseJoy));
        when(passwordEncoder.matches("wrong_password", "encoded_password")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(request));

        verify(passwordEncoder).matches("wrong_password", "encoded_password");
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