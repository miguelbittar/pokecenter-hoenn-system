package com.hoenn.pokecenter.components;

import com.hoenn.pokecenter.entity.NurseJoy;
import com.hoenn.pokecenter.entity.Pokemon;
import com.hoenn.pokecenter.repository.NurseJoyRepository;
import com.hoenn.pokecenter.repository.PokemonRepository;
import org.springframework.stereotype.Component;

@Component
public class BusinessIdGenerator {

    private final NurseJoyRepository nurseJoyRepository;
    private final PokemonRepository pokemonRepository;

    public BusinessIdGenerator(NurseJoyRepository nurseJoyRepository, PokemonRepository pokemonRepository) {
        this.nurseJoyRepository = nurseJoyRepository;
        this.pokemonRepository = pokemonRepository;
    }

    public String generateSequentialNurseJoyId() {
        String lastNurseJoyId = nurseJoyRepository.findTopByIsDeletedFalseOrderByCreatedAtDesc()
                .map(NurseJoy::getNurseJoyId)
                .orElse("JOY000000");
        int nextNumber = Integer.parseInt(lastNurseJoyId.substring(3)) + 1;
        return "JOY" + String.format("%06d", nextNumber);
    }

    public String generateSequentialPokemonId() {
        String lastPokemonId = pokemonRepository.findTopByIsDeletedFalseOrderByCreatedAtDesc()
                .map(Pokemon::getPokemonId)
                .orElse("PKM000000");
        int nextNumber = Integer.parseInt(lastPokemonId.substring(3)) + 1;
        return "PKM" + String.format("%06d", nextNumber);
    }
}
