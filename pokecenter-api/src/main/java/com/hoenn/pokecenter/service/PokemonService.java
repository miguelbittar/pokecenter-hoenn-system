package com.hoenn.pokecenter.service;

import com.hoenn.pokecenter.components.BusinessIdGenerator;
import com.hoenn.pokecenter.dto.request.PokemonUpdateRequest;
import com.hoenn.pokecenter.dto.response.PokemonResponse;
import com.hoenn.pokecenter.entity.NurseJoy;
import com.hoenn.pokecenter.entity.Pokemon;
import com.hoenn.pokecenter.enums.PokemonStatus;
import com.hoenn.pokecenter.exception.custom.InvalidPokemonSpeciesException;
import com.hoenn.pokecenter.exception.custom.PokemonNotFoundException;
import com.hoenn.pokecenter.external.PokeApiClient;
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
    private final PokeApiClient pokeApiClient;

    public PokemonService(PokemonRepository pokemonRepository, BusinessIdGenerator businessIdGenerator, NurseJoyService nurseJoyService, PokeApiClient pokeApiClient) {
        this.pokemonRepository = pokemonRepository;
        this.businessIdGenerator = businessIdGenerator;
        this.nurseJoyService = nurseJoyService;
        this.pokeApiClient = pokeApiClient;
    }

    public Pokemon registerPokemon(Pokemon pokemon){

        if(!pokeApiClient.validateSpecies(pokemon.getSpecies())){
            throw new InvalidPokemonSpeciesException(pokemon.getSpecies());
        }

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

    public Pokemon updatePokemonProfile(String pokemonId, PokemonUpdateRequest updateRequest) {
        Pokemon existingPokemon = findByPokemonId(pokemonId);

        if (updateRequest.species() != null &&
                !updateRequest.species().equals(existingPokemon.getSpecies())){

            if (!pokeApiClient.validateSpecies(updateRequest.species())) {
                throw new InvalidPokemonSpeciesException(updateRequest.species());
            }
        }

        if (updateRequest.responsibleJoyId() != null) {
            NurseJoy newJoy = nurseJoyService.findByNurseJoyId(updateRequest.responsibleJoyId());
            existingPokemon.setResponsibleJoy(newJoy);
        }

        Optional.ofNullable(updateRequest.name()).ifPresent(existingPokemon::setName);
        Optional.ofNullable(updateRequest.species()).ifPresent(existingPokemon::setSpecies);
        Optional.ofNullable(updateRequest.trainerId()).ifPresent(existingPokemon::setTrainerId);
        Optional.ofNullable(updateRequest.condition()).ifPresent(existingPokemon::setCondition);
        Optional.ofNullable(updateRequest.status()).ifPresent(existingPokemon::setStatus);
        Optional.ofNullable(updateRequest.releaseDate()).ifPresent(existingPokemon::setReleaseDate);

        return pokemonRepository.save(existingPokemon);
    }

    @Transactional
    public void deleteByPokemonId(String pokemonId){
        Pokemon existingPokemon = findByPokemonId(pokemonId);
        existingPokemon.softDelete();
        pokemonRepository.save(existingPokemon);
    }
}
