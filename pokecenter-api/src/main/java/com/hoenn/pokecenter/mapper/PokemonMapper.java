package com.hoenn.pokecenter.mapper;


import com.hoenn.pokecenter.dto.request.PokemonRequest;
import com.hoenn.pokecenter.dto.response.PokemonResponse;
import com.hoenn.pokecenter.entity.NurseJoy;
import com.hoenn.pokecenter.entity.Pokemon;

public final class PokemonMapper {

    private PokemonMapper(){}

    public static Pokemon toEntity(PokemonRequest request, NurseJoy nurseJoy){
        return new Pokemon(
                request.name(),
                request.species(),
                request.trainerId(),
                request.condition(),
                nurseJoy
        );
    }

    public static PokemonResponse toResponse(Pokemon pokemon) {
        return new PokemonResponse(
                pokemon.getPokemonId(),
                pokemon.getName(),
                pokemon.getSpecies(),
                pokemon.getTrainerId(),
                pokemon.getCondition(),
                pokemon.getStatus(),
                pokemon.getAdmissionDate(),
                pokemon.getReleaseDate(),
                pokemon.getResponsibleJoy().getNurseJoyId(),
                pokemon.getResponsibleJoy().getName(),
                pokemon.getCreatedAt()
        );
    }
}
