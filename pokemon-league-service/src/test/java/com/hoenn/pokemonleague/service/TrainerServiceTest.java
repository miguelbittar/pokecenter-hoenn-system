package com.hoenn.pokemonleague.service;

import com.hoenn.pokemonleague.components.BusinessIdGenerator;
import com.hoenn.pokemonleague.entity.Trainer;
import com.hoenn.pokemonleague.enums.TrainerRegion;
import com.hoenn.pokemonleague.exceptions.custom.TrainerNotFoundException;
import com.hoenn.pokemonleague.repository.TrainerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private BusinessIdGenerator businessIdGenerator;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    @DisplayName("Should generate trainer ID and save trainer successfully")
    void registerTrainerCase1() {

        Trainer mockTrainer = new Trainer("Test", "test@email.com", TrainerRegion.HOENN);

        when(businessIdGenerator.generateRandomTrainerId(TrainerRegion.HOENN)).thenReturn("HN123456");
        when(trainerRepository.save(any(Trainer.class))).thenReturn(mockTrainer);

        Trainer result = trainerService.registerTrainer(mockTrainer);

        assertThat(result.getTrainerId()).isNotNull();
        verify(businessIdGenerator).generateRandomTrainerId(TrainerRegion.HOENN);
        verify(trainerRepository).save(mockTrainer);
    }

    @Test
    @DisplayName("Should return trainer when trainer ID exists")
    void findByTrainerIdCase1() {
        Trainer mockTrainer = new Trainer("Test", "test@email.com", TrainerRegion.HOENN);
        mockTrainer.setTrainerId("HN123456");

        when(trainerRepository.findByTrainerIdAndIsDeletedFalse(mockTrainer.getTrainerId())).thenReturn(Optional.of(mockTrainer));

        Trainer result = trainerService.findByTrainerId(mockTrainer.getTrainerId());

        assertThat(result).isEqualTo(mockTrainer);
        verify(trainerRepository).findByTrainerIdAndIsDeletedFalse("HN123456");
    }

    @Test
    @DisplayName("Should throw TrainerNotFoundException when trainer not found")
    void findByTrainerIdCase2(){

        String mockTrainerId = "HN123456";
        when(trainerRepository.findByTrainerIdAndIsDeletedFalse(mockTrainerId)).thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class,
                () -> trainerService.findByTrainerId(mockTrainerId));
    }
}