package com.hoenn.pokecenter.service;

import com.hoenn.pokecenter.components.BusinessIdGenerator;
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

    public PokemonService(PokemonRepository pokemonRepository, BusinessIdGenerator businessIdGenerator) {
        this.pokemonRepository = pokemonRepository;
        this.businessIdGenerator = businessIdGenerator;
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

    public Pokemon updatePokemonProfile (String pokemonId, Pokemon updateProfile){
        Pokemon existingPokemon = findByPokemonId(pokemonId);

        Optional.ofNullable(updateProfile.getName()).ifPresent(existingPokemon::setName);
        Optional.ofNullable(updateProfile.getSpecies()).ifPresent(existingPokemon::setSpecies);
        Optional.ofNullable(updateProfile.getTrainerId()).ifPresent(existingPokemon::setTrainerId);
        Optional.ofNullable(updateProfile.getCondition()).ifPresent(existingPokemon::setCondition);
        Optional.ofNullable(updateProfile.getStatus()).ifPresent(existingPokemon::setStatus);
        Optional.ofNullable(updateProfile.getResponsibleJoy()).ifPresent(existingPokemon::setResponsibleJoy);

        return pokemonRepository.save(existingPokemon);
    }

    @Transactional
    public void deleteByPokemonId(String pokemonId){
        Pokemon existingPokemon = findByPokemonId(pokemonId);
        existingPokemon.softDelete();
        pokemonRepository.save(existingPokemon);
    }
}
