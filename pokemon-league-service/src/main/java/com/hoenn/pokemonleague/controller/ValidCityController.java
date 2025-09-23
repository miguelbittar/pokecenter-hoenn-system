package com.hoenn.pokemonleague.controller;

import com.hoenn.pokemonleague.dto.request.ValidCityRequest;
import com.hoenn.pokemonleague.dto.response.ValidCityResponse;
import com.hoenn.pokemonleague.entity.ValidCity;
import com.hoenn.pokemonleague.mapper.ValidCityMapper;
import com.hoenn.pokemonleague.service.ValidCityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pokemon-league/valid-cities")
public class ValidCityController {

    private final ValidCityService validCityService;

    public ValidCityController(ValidCityService validCityService) {
        this.validCityService = validCityService;
    }

    @PostMapping
    public ResponseEntity<ValidCityResponse> registerValidCity(@Valid @RequestBody ValidCityRequest request) {
        ValidCity validCity = ValidCityMapper.toEntity(request);
        ValidCity registeredCity = validCityService.registerValidCity(validCity);
        ValidCityResponse response = ValidCityMapper.toResponse(registeredCity);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ValidCityResponse>> getAllValidCities() {
        List<ValidCity> validCities = validCityService.getAllValidCities();
        List<ValidCityResponse> responses = validCities.stream()
                .map(ValidCityMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }
}
