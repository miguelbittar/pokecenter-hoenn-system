package com.hoenn.pokecenter.service;

import com.hoenn.pokecenter.components.BusinessIdGenerator;
import com.hoenn.pokecenter.dto.request.PokemonUpdateRequest;
import com.hoenn.pokecenter.dto.response.RVPValidationResponse;
import com.hoenn.pokecenter.entity.NurseJoy;
import com.hoenn.pokecenter.entity.Pokemon;
import com.hoenn.pokecenter.enums.PokemonStatus;
import com.hoenn.pokecenter.exception.custom.InvalidPokemonSpeciesException;
import com.hoenn.pokecenter.exception.custom.InvalidRVPException;
import com.hoenn.pokecenter.exception.custom.PokemonNotFoundException;
import com.hoenn.pokecenter.exception.custom.TrainerNotRegisteredException;
import com.hoenn.pokecenter.external.PokeApiClient;
import com.hoenn.pokecenter.external.PokemonLeagueClient;
import com.hoenn.pokecenter.repository.PokemonRepository;
import com.hoenn.pokecenter.utils.TrainerIdUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PokemonService {

    private static final Logger log = LoggerFactory.getLogger(PokemonService.class);
    private final PokemonRepository pokemonRepository;
    private final BusinessIdGenerator businessIdGenerator;
    private final NurseJoyService nurseJoyService;
    private final PokeApiClient pokeApiClient;
    private final PokemonLeagueClient pokemonLeagueClient;

    public PokemonService(PokemonRepository pokemonRepository, BusinessIdGenerator businessIdGenerator, NurseJoyService nurseJoyService, PokeApiClient pokeApiClient, PokemonLeagueClient pokemonLeagueClient) {
        this.pokemonRepository = pokemonRepository;
        this.businessIdGenerator = businessIdGenerator;
        this.nurseJoyService = nurseJoyService;
        this.pokeApiClient = pokeApiClient;
        this.pokemonLeagueClient = pokemonLeagueClient;
    }

    public Pokemon registerPokemon(Pokemon pokemon){

        validateTrainerId(pokemon.getTrainerId());

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

    private void validateTrainerId(String trainerId) {
        String trainerRegion = TrainerIdUtils.extractRegion(trainerId);

        if (!pokemonLeagueClient.validateTrainerId(trainerId)) {
            throw new TrainerNotRegisteredException(
                    "Trainer ID '" + trainerId + "' is not registered in Pokemon League system"
            );
        }

        if (TrainerIdUtils.isFromHoenn(trainerId)) {
            log.debug("Trainer {} from Hoenn region validated successfully", trainerId);
            return;
        }

        log.debug("Trainer {} is from {} region, validating RVP for Hoenn access", trainerId, trainerRegion);
        validateForeignTrainerRVP(trainerId, trainerRegion);
    }

    private void validateForeignTrainerRVP(String trainerId, String trainerRegion) {
        RVPValidationResponse rvpResponse = pokemonLeagueClient.validateTrainerRVP(trainerId, "HOENN");

        if (!rvpResponse.hasValidRVP()) {
            throw new InvalidRVPException(
                    "Trainer from " + trainerRegion + " region requires a valid RVP to access Hoenn services. " +
                            "Please obtain a Region Visitor Passport from Pokemon League. " + rvpResponse.message()
            );
        }

        log.debug("Foreign trainer {} validated successfully with RVP status: {}",
                trainerId, rvpResponse.status());
    }
}
