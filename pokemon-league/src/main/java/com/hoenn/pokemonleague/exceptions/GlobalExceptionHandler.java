package com.hoenn.pokemonleague.exceptions;

import com.hoenn.pokemonleague.exceptions.custom.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TrainerNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTrainerNotFoundException(TrainerNotFoundException exception) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "TRAINER_NOT_FOUND");
        response.put("message", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRVPRequestException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidRVPRequestException(InvalidRVPRequestException exception) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "INVALID_RVP_REQUEST");
        response.put("message", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateRVPException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateRVPException(DuplicateRVPException exception) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "DUPLICATE_RVP");
        response.put("message", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ValidCityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleValidCityNotFoundException(ValidCityNotFoundException exception) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "VALID_CITY_NOT_FOUND");
        response.put("message", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RVPNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleRVPNotFoundException(RVPNotFoundException exception) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "RVP_NOT_FOUND");
        response.put("message", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
