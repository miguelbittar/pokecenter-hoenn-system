package com.hoenn.pokecenter.service;

import com.hoenn.pokecenter.components.BusinessIdGenerator;
import com.hoenn.pokecenter.dto.request.PokemonUpdateRequest;
import com.hoenn.pokecenter.dto.response.PokemonResponse;
import com.hoenn.pokecenter.entity.NurseJoy;
import com.hoenn.pokecenter.entity.Pokemon;
import com.hoenn.pokecenter.enums.PokemonStatus;
import com.hoenn.pokecenter.exception.custom.PokemonNotFoundException;
import com.hoenn.pokecenter.repository.PokemonRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PokemonService {

    private final PokemonRepository pokemonRepository;
    private final BusinessIdGenerator businessIdGenerator;
    private final NurseJoyService nurseJoyService;

    public PokemonService(PokemonRepository pokemonRepository, BusinessIdGenerator businessIdGenerator, NurseJoyService nurseJoyService) {
        this.pokemonRepository = pokemonRepository;
        this.businessIdGenerator = businessIdGenerator;
        this.nurseJoyService = nurseJoyService;
    }

    public Pokemon registerPokemon(Pokemon pokemon){
        /*
        TODO: Validate trainerId in Pokémon League + RVP
        TODO: Validate species in PokéAPI
        */
        pokemon.setPokemonId(businessIdGenerator.generateSequentialPokemonId());
        pokemon.setStatus(PokemonStatus.ADMISSION);
        return pokemonRepository.save(pokemon);
    }

    public List<Pokemon> getAllPokemons(){
        return pokemonRepository.findByIsDeletedFalse();
    }

    public Pokemon findByPokemonId(String pokemonId){
        return pokemonRepository.findByPokemonIdAndIsDeletedFalse(pokemonId)
                .orElseThrow(() -> new PokemonNotFoundException("No Pokémon found with ID: " + pokemonId));
    }

    public Pokemon updatePokemonProfile(String pokemonId, PokemonUpdateRequest request) {
        Pokemon existingPokemon = findByPokemonId(pokemonId);

        if (request.responsibleJoyId() != null) {
            NurseJoy newJoy = nurseJoyService.findByNurseJoyId(request.responsibleJoyId());
            existingPokemon.setResponsibleJoy(newJoy);
        }

        Optional.ofNullable(request.name()).ifPresent(existingPokemon::setName);
        Optional.ofNullable(request.species()).ifPresent(existingPokemon::setSpecies);
        Optional.ofNullable(request.trainerId()).ifPresent(existingPokemon::setTrainerId);
        Optional.ofNullable(request.condition()).ifPresent(existingPokemon::setCondition);
        Optional.ofNullable(request.status()).ifPresent(existingPokemon::setStatus);
        Optional.ofNullable(request.releaseDate()).ifPresent(existingPokemon::setReleaseDate);

        return pokemonRepository.save(existingPokemon);
    }

    @Transactional
    public void deleteByPokemonId(String pokemonId){
        Pokemon existingPokemon = findByPokemonId(pokemonId);
        existingPokemon.softDelete();
        pokemonRepository.save(existingPokemon);
    }
}
