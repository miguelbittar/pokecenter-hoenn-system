package com.hoenn.pokemonleague.controller;

import com.hoenn.pokemonleague.dto.request.TrainerRequest;
import com.hoenn.pokemonleague.dto.response.TrainerResponse;
import com.hoenn.pokemonleague.entity.Trainer;
import com.hoenn.pokemonleague.mapper.TrainerMapper;
import com.hoenn.pokemonleague.service.TrainerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pokemon-league/trainers")
public class TrainerController {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping
    public ResponseEntity<TrainerResponse> registerTrainer(@Valid @RequestBody TrainerRequest request) {
        Trainer savedTrainer = trainerService.registerTrainer(TrainerMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TrainerMapper.toResponse(savedTrainer));
    }
}
