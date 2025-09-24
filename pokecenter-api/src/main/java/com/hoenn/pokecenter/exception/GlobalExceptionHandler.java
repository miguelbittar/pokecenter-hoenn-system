package com.hoenn.pokecenter.exception;

import com.hoenn.pokecenter.exception.custom.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NurseJoyNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNurseJoyNotFoundException(NurseJoyNotFoundException exception) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "NURSE_JOY_NOT_FOUND");
        response.put("message", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PokemonNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePokemonNotFoundException(PokemonNotFoundException exception) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "POKEMON_NOT_FOUND");
        response.put("message", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "EMAIL_ALREADY_EXISTS");
        response.put("message", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedRoleChangeException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedRoleChangeException(UnauthorizedRoleChangeException exception){
        Map<String, Object> response = new HashMap<>();
        response.put("error", "UNAUTHORIZED_ROLE_CHANGE");
        response.put("message", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(PasswordChangeNotAllowedException.class)
    public ResponseEntity<Map<String, Object>> handlePasswordChangeNotAllowedException(PasswordChangeNotAllowedException exception){
        Map<String, Object> response = new HashMap<>();
        response.put("error", "PASSWORD_CHANGE_NOT_ALLOWED");
        response.put("message", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentialsException(InvalidCredentialsException exception){
        Map<String, Object> response = new HashMap<>();
        response.put("error", "INVALID_CREDENTIALS");
        response.put("message", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedOperationException(UnauthorizedOperationException exception){
        Map<String, Object> response = new HashMap<>();
        response.put("error", "UNAUTHORIZED_OPERATION");
        response.put("message", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidPokemonSpeciesException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidPokemonSpeciesException(InvalidPokemonSpeciesException exception) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "INVALID_POKEMON_SPECIES");
        response.put("message", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTrainerIdFormatException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidTrainerIdFormatException(
            InvalidTrainerIdFormatException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Invalid Trainer ID Format");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(TrainerNotRegisteredException.class)
    public ResponseEntity<Map<String, Object>> handleTrainerNotRegisteredException(
            TrainerNotRegisteredException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Trainer Not Registered");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidRVPException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidRVPException(
            InvalidRVPException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Invalid Region Visitor Passport");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException exception) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage())
                );
        response.put("error", "VALIDATION_FAILED");
        response.put("fieldErrors", fieldErrors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
