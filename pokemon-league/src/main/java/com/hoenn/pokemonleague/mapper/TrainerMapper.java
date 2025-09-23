package com.hoenn.pokemonleague.mapper;

import com.hoenn.pokemonleague.dto.request.TrainerRequest;
import com.hoenn.pokemonleague.dto.response.TrainerResponse;
import com.hoenn.pokemonleague.entity.Trainer;

public final class TrainerMapper {

    private TrainerMapper(){}

    public static Trainer toEntity(TrainerRequest request) {
        return new Trainer(
                request.name(),
                request.email(),
                request.region()
        );
    }

    public static TrainerResponse toResponse(Trainer trainer) {
        return new TrainerResponse(
                trainer.getTrainerId(),
                trainer.getName(),
                trainer.getEmail(),
                trainer.getRegion(),
                trainer.getCreatedAt()
        );
    }
}