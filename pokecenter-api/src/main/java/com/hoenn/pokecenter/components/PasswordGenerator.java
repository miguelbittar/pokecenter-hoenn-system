package com.hoenn.pokecenter.components;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PasswordGenerator {

    private static final String ALPHANUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final SecureRandom secureRandom;

    public PasswordGenerator() {
        this.secureRandom = new SecureRandom();
    }

    public String generateTemporaryPassword(){
        return generateAlphanumeric(8);
    }

    private String generateAlphanumeric(int length){
        StringBuilder temporaryPassword = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            temporaryPassword.append(ALPHANUMERIC.charAt(secureRandom.nextInt(ALPHANUMERIC.length())));
        }
        return temporaryPassword.toString();
    }
}
