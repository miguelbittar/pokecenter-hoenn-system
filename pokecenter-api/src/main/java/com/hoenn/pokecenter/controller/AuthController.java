package com.hoenn.pokecenter.controller;

import com.hoenn.pokecenter.dto.request.LoginRequest;
import com.hoenn.pokecenter.dto.response.LoginResponse;
import com.hoenn.pokecenter.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pokecenter/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login (@Valid @RequestBody LoginRequest loginRequest){
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
