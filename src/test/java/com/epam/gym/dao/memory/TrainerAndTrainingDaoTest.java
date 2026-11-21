package com.epam.gym.dao.memory;

import com.epam.gym.model.Trainer;
import com.epam.gym.model.Training;
import com.epam.gym.storage.InMemoryStorage;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainerAndTrainingDaoTest {
    @Test
    void trainerDaoSavesFindsAndFindsMatchingUsernames() {
        final var storage = new InMemoryStorage<Trainer>("trainer");
        final var dao = new TrainerDaoImpl();
        dao.setStorage(storage);

        final var trainer = new Trainer();
        trainer.setFirstName("Alex");
        trainer.setLastName("River");
        trainer.setUsername("Alex.River");

        final var saved = dao.save(trainer);

        assertEquals(1L, saved.getId());
        assertEquals(saved, dao.findById(1L).orElseThrow());
        assertEquals(Set.of("Alex.River"), dao.findUsernamesStartingWith("Alex."));
    }

    @Test
    void trainingDaoSavesAndFindsAll() {
        final var storage = new InMemoryStorage<Training>("training");
        final var dao = new TrainingDaoImpl();
        dao.setStorage(storage);

        final var training = new Training();
        training.setTrainingName("Cardio");
        final var saved = dao.save(training);

        assertEquals(1L, saved.getId());
        assertTrue(dao.findAll().contains(saved));
        assertEquals(saved, dao.findById(1L).orElseThrow());
    }
}
