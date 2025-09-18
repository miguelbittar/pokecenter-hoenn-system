package com.hoenn.pokecenter.exception.custom;

public class UnauthorizedRoleChangeException extends RuntimeException {
    public UnauthorizedRoleChangeException(String message) {

        super(message);
    }
}
