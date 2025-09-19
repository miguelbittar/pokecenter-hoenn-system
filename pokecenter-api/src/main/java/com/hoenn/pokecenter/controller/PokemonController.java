package com.hoenn.pokecenter.controller;

import com.hoenn.pokecenter.entity.Pokemon;
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

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @PostMapping
    public ResponseEntity<Pokemon> registerPokemon(@Valid @RequestBody Pokemon pokemon,
            @RequestParam String responsibleJoyId) {

        Pokemon savedPokemon = pokemonService.registerPokemon(pokemon, responsibleJoyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPokemon);
    }

    @GetMapping
    public ResponseEntity<List<Pokemon>> getAllPokemons(){
        return ResponseEntity.ok(pokemonService.getAllPokemons()
                .stream()
                .toList());
    }

    @GetMapping("/{pokemonId}")
    public ResponseEntity<Pokemon> getPokemonByPokemonId(@PathVariable String pokemonId) {
        Pokemon pokemon = pokemonService.findByPokemonId(pokemonId);
        return ResponseEntity.ok(pokemon);
    }

    @PutMapping("/{pokemonId}/profile")
    public ResponseEntity<Pokemon> updatePokemonProfile(
            @PathVariable String pokemonId, @Valid @RequestBody Pokemon request){
        Pokemon updatePokemon = pokemonService.updatePokemonProfile(pokemonId, request);
        return ResponseEntity.ok(updatePokemon);
    }

    @DeleteMapping("/{pokemonId}")
    public ResponseEntity<Void> deletePokemon(@PathVariable String pokemonId){
        pokemonService.deleteByPokemonId(pokemonId);
        return ResponseEntity.noContent().build();
    }
}
