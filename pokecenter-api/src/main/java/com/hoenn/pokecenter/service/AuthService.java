package com.hoenn.pokecenter.service;

import com.hoenn.pokecenter.dto.request.LoginRequest;
import com.hoenn.pokecenter.dto.response.LoginResponse;
import com.hoenn.pokecenter.entity.NurseJoy;
import com.hoenn.pokecenter.exception.custom.InvalidCredentialsException;
import com.hoenn.pokecenter.repository.NurseJoyRepository;
import com.hoenn.pokecenter.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final NurseJoyRepository nurseJoyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(NurseJoyRepository nurseJoyRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.nurseJoyRepository = nurseJoyRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    public LoginResponse login (LoginRequest request){
        NurseJoy nurseJoy = nurseJoyRepository.findByNurseJoyIdAndIsDeletedFalse(request.nurseJoyId())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.password(), nurseJoy.getPassword())){
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String token = jwtService.generateToken(nurseJoy);
        return new LoginResponse(
                token,
                nurseJoy.getNurseJoyId(),
                nurseJoy.getRole()
        );
    }
}
