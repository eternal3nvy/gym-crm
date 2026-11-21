package com.epam.gym.dao.memory;

import com.epam.gym.model.Trainee;
import com.epam.gym.storage.InMemoryStorage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class
InMemoryDaoTest {
    @Test
    void saveFindAndDeleteTrainee() {
        final var storage = new InMemoryStorage<Trainee>("trainee");
        final var dao = new TraineeDaoImpl();
        dao.setStorage(storage);
        final var trainee = new Trainee();
        trainee.setFirstName("Jane");
        trainee.setLastName("Doe");

        final var saved = dao.save(trainee);

        assertEquals(1L, saved.getId());
        assertEquals(saved, dao.findById(1L).orElseThrow());
        assertTrue(dao.deleteById(1L));
        assertTrue(dao.findAll().isEmpty());
    }
}
