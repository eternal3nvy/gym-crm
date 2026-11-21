package com.epam.gym.service;

import com.epam.gym.dao.TraineeDao;
import com.epam.gym.dao.TrainerDao;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.TrainingType;
import com.epam.gym.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {
    @Mock
    private TrainerDao trainerDao;
    @Mock
    private TraineeDao traineeDao;

    @Test
    void createGeneratesUniqueCredentialsAndUpdatePreservesThem() {
        final var service = new TrainerServiceImpl(
                trainerDao, new ProfileCredentialGenerator(traineeDao, trainerDao));

        when(trainerDao.findUsernamesStartingWith("Alex.River"))
                .thenReturn(Set.of("Alex.River"));
        when(traineeDao.findUsernamesStartingWith("Alex.River")).thenReturn(Set.of());
        when(trainerDao.save(any(Trainer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final var trainer = new Trainer();
        trainer.setFirstName("Alex");
        trainer.setLastName("River");
        trainer.setSpecialization(new TrainingType("Fitness"));
        final var created = service.create(trainer);
        assertEquals("Alex.River1", created.getUsername());
        assertEquals(10, created.getPassword().length());

        created.setId(3L);
        final var existing = new Trainer();
        existing.setId(3L);
        existing.setUsername(created.getUsername());
        existing.setPassword(created.getPassword());
        when(trainerDao.findById(3L)).thenReturn(Optional.of(existing));

        final var update = new Trainer();
        update.setId(3L);
        update.setFirstName("Alex");
        update.setLastName("River");
        final var updated = service.update(update);
        assertEquals(created.getUsername(), updated.getUsername());
        assertEquals(created.getPassword(), updated.getPassword());
    }

    @Test
    void findByIdAndFindAllDelegateToDao() {
        final var trainer = new Trainer();
        trainer.setId(8L);
        final var service = new TrainerServiceImpl(
                trainerDao, new ProfileCredentialGenerator(traineeDao, trainerDao));
        when(trainerDao.findById(8L)).thenReturn(Optional.of(trainer));
        when(trainerDao.findAll()).thenReturn(List.of(trainer));

        assertEquals(trainer, service.findById(8L).orElseThrow());
        assertEquals(List.of(trainer), service.findAll());
    }

    @Test
    void rejectsBlankNamesAndMissingUpdateId() {
        final var service = new TrainerServiceImpl(
                trainerDao, new ProfileCredentialGenerator(traineeDao, trainerDao));
        final var blank = new Trainer();
        blank.setFirstName("");
        blank.setLastName("River");
        assertThrows(IllegalArgumentException.class, () -> service.create(blank));
        verify(trainerDao, never()).save(any());

        final var missingId = new Trainer();
        missingId.setFirstName("Alex");
        missingId.setLastName("River");
        assertThrows(IllegalArgumentException.class, () -> service.update(missingId));
    }

    @Test
    void updateRejectsUnknownProfile() {
        final var service = new TrainerServiceImpl(
                trainerDao, new ProfileCredentialGenerator(traineeDao, trainerDao));
        final var update = new Trainer();
        update.setId(99L);
        update.setFirstName("Alex");
        update.setLastName("River");
        when(trainerDao.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(update));
    }
}
