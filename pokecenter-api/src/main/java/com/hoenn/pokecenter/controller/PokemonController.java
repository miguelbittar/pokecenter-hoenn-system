package com.hoenn.pokecenter.controller;

import com.hoenn.pokecenter.dto.request.PokemonRequest;
import com.hoenn.pokecenter.dto.request.PokemonUpdateRequest;
import com.hoenn.pokecenter.dto.response.PokemonResponse;
import com.hoenn.pokecenter.entity.NurseJoy;
import com.hoenn.pokecenter.entity.Pokemon;
import com.hoenn.pokecenter.mapper.PokemonMapper;
import com.hoenn.pokecenter.service.NurseJoyService;
import com.hoenn.pokecenter.service.PokemonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pokecenter/pokemon")
public class PokemonController {

    private final PokemonService pokemonService;
    private final NurseJoyService nurseJoyService;

    public PokemonController(PokemonService pokemonService, NurseJoyService nurseJoyService) {
        this.pokemonService = pokemonService;
        this.nurseJoyService = nurseJoyService;
    }

    @PostMapping
    public ResponseEntity<PokemonResponse> registerPokemon(@Valid @RequestBody PokemonRequest request) {
        NurseJoy nurseJoy= nurseJoyService.findByNurseJoyId(request.responsibleJoyId());
        Pokemon savedPokemon = pokemonService.registerPokemon(PokemonMapper.toEntity(request, nurseJoy));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PokemonMapper.toResponse(savedPokemon));
    }

    @GetMapping
    public ResponseEntity<List<PokemonResponse>> getAllPokemons(){
        return ResponseEntity.ok(pokemonService.getAllPokemons()
                .stream()
                .map(PokemonMapper::toResponse)
                .toList());
    }

    @GetMapping("/{pokemonId}")
    public ResponseEntity<PokemonResponse> getPokemonByPokemonId(@PathVariable String pokemonId) {
        Pokemon pokemon = pokemonService.findByPokemonId(pokemonId);
        return ResponseEntity.ok(PokemonMapper.toResponse(pokemon));
    }

    @PutMapping("/{pokemonId}/profile")
    public ResponseEntity<PokemonResponse> updatePokemonProfile(@PathVariable String pokemonId,
            @Valid @RequestBody PokemonUpdateRequest request) {

        Pokemon updatePokemon = pokemonService.updatePokemonProfile(pokemonId, request);
        return ResponseEntity.ok(PokemonMapper.toResponse(updatePokemon));
    }

    @DeleteMapping("/{pokemonId}")
    public ResponseEntity<Void> deletePokemon(@PathVariable String pokemonId){
        pokemonService.deleteByPokemonId(pokemonId);
        return ResponseEntity.noContent().build();
    }
}
