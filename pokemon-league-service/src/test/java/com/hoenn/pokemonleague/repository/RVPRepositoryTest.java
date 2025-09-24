package com.hoenn.pokemonleague.repository;

import com.hoenn.pokemonleague.entity.RVP;
import com.hoenn.pokemonleague.entity.Trainer;
import com.hoenn.pokemonleague.entity.ValidCity;
import com.hoenn.pokemonleague.enums.AuthorityType;
import com.hoenn.pokemonleague.enums.TrainerRegion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
class RVPRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RVPRepository rvpRepository;

    @Test
    @DisplayName("Should get RVP successfully from DB")
    void findActiveRVPByTrainerAndTargetRegionCase1() {

        Trainer trainer = this.createTrainer("KT123456");
        ValidCity city = this.createValidCity();
        RVP activeRvp = this.createRVP(trainer, TrainerRegion.HOENN, city);

        entityManager.persistAndFlush(trainer);
        entityManager.persistAndFlush(city);
        entityManager.persistAndFlush(activeRvp);

        Optional<RVP> result = this.rvpRepository.findActiveRVPByTrainerAndTargetRegion("KT123456", TrainerRegion.HOENN);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not get RVP from DB when RVP not exists")
    void findActiveRVPByTrainerAndTargetRegionCase2() {
        Optional<RVP> result = this.rvpRepository.findActiveRVPByTrainerAndTargetRegion("KT123456", TrainerRegion.HOENN);
        assertThat(result.isEmpty()).isTrue();
    }

    private Trainer createTrainer(String trainerId) {
        Trainer trainer = new Trainer("Test Trainer", "test@email.com", TrainerRegion.KANTO);
        trainer.setTrainerId(trainerId);
        return trainer;
    }

    private ValidCity createValidCity() {
        return new ValidCity("test-city", TrainerRegion.HOENN, AuthorityType.POKECENTER);
    }

    private RVP createRVP(Trainer trainer, TrainerRegion targetRegion, ValidCity city) {
        RVP rvp = new RVP(trainer, targetRegion, city);
        rvp.setRvpId("RVP123456-HN");
        return rvp;
    }
}