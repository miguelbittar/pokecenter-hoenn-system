package com.hoenn.pokemonleague.service;

import com.hoenn.pokemonleague.components.BusinessIdGenerator;
import com.hoenn.pokemonleague.entity.Trainer;
import com.hoenn.pokemonleague.repository.TrainerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final BusinessIdGenerator businessIdGenerator;

    public TrainerService(TrainerRepository trainerRepository, BusinessIdGenerator businessIdGenerator) {
        this.trainerRepository = trainerRepository;
        this.businessIdGenerator = businessIdGenerator;
    }

    public Trainer registerTrainer(Trainer trainer){
        trainer.setTrainerId(businessIdGenerator.generateRandomTrainerId(trainer.getRegion()));
        return trainerRepository.save(trainer);
    }
}
