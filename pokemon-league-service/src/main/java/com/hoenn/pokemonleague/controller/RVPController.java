package com.hoenn.pokemonleague.controller;

import com.hoenn.pokemonleague.dto.request.RVPRequest;
import com.hoenn.pokemonleague.dto.response.RVPResponse;
import com.hoenn.pokemonleague.dto.response.RVPStatusResponse;
import com.hoenn.pokemonleague.entity.RVP;
import com.hoenn.pokemonleague.enums.TrainerRegion;
import com.hoenn.pokemonleague.mapper.RVPMapper;
import com.hoenn.pokemonleague.service.RVPService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pokemon-league/rvp")
public class RVPController {

    private final RVPService rvpService;

    public RVPController(RVPService rvpService) {
        this.rvpService = rvpService;
    }

    @PostMapping
    public ResponseEntity<RVPResponse> issueRVP(@Valid @RequestBody RVPRequest request) {
        RVP savedRVP = rvpService.issueRVP(request.trainerId(), request.targetRegion(), request.cityName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RVPMapper.toResponse(savedRVP));
    }

    @GetMapping("/{trainerId}/status")
    public ResponseEntity<RVPStatusResponse> validateRVPStatus(@PathVariable String trainerId,
            @RequestParam TrainerRegion targetRegion) {

        RVPStatusResponse statusResponse = rvpService.validateRVPStatus(trainerId, targetRegion);
        return ResponseEntity.ok(statusResponse);
    }
}
