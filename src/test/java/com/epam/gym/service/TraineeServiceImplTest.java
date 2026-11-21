package com.epam.gym.service;

import com.epam.gym.dao.TraineeDao;
import com.epam.gym.dao.TrainerDao;
import com.epam.gym.exception.EntityNotFoundException;
import com.epam.gym.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {
    @Mock
    private TraineeDao traineeDao;
    @Mock
    private TrainerDao trainerDao;

    private TraineeServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TraineeServiceImpl(
                traineeDao, new ProfileCredentialGenerator(traineeDao, trainerDao));
    }

    @Test
    void createGeneratesUsernameAndPassword() {
        final var trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Smith");
        when(traineeDao.findUsernamesStartingWith("John.Smith")).thenReturn(Set.of());
        when(trainerDao.findUsernamesStartingWith("John.Smith")).thenReturn(Set.of());
        when(traineeDao.save(any(Trainee.class))).thenAnswer(invocation -> {
            final var saved = invocation.<Trainee>getArgument(0);
            saved.setId(1L);
            return saved;
        });

        final var created = service.create(trainee);

        assertEquals("John.Smith", created.getUsername());
        assertEquals(10, created.getPassword().length());
        verify(traineeDao).save(created);
    }

    @Test
    void createUsesAvailableSerialSuffix() {
        final var trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Smith");
        when(traineeDao.findUsernamesStartingWith("John.Smith"))
                .thenReturn(Set.of("John.Smith", "John.Smith1"));
        when(trainerDao.findUsernamesStartingWith("John.Smith")).thenReturn(Set.of());
        when(traineeDao.save(any(Trainee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final var created = service.create(trainee);

        assertEquals("John.Smith2", created.getUsername());
    }

    @Test
    void updatePreservesCredentialsAndRequiresExistingProfile() {
        final var existing = new Trainee();
        existing.setId(7L);
        existing.setUsername("John.Smith");
        existing.setPassword("existing12");
        when(traineeDao.findById(7L)).thenReturn(Optional.of(existing));
        when(traineeDao.save(any(Trainee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final var update = new Trainee();
        update.setId(7L);
        update.setFirstName("John");
        update.setLastName("Smith");
        final var saved = service.update(update);

        assertEquals("John.Smith", saved.getUsername());
        assertEquals("existing12", saved.getPassword());
    }

    @Test
    void updateRejectsUnknownProfile() {
        final var update = new Trainee();
        update.setId(99L);
        update.setFirstName("John");
        update.setLastName("Smith");
        when(traineeDao.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(update));
    }

    @Test
    void deleteFindByIdAndFindAllDelegateToDao() {
        when(traineeDao.deleteById(4L)).thenReturn(true);
        when(traineeDao.findById(4L)).thenReturn(Optional.empty());
        when(traineeDao.findAll()).thenReturn(List.of());

        assertEquals(true, service.delete(4L));
        assertEquals(Optional.empty(), service.findById(4L));
        assertEquals(List.of(), service.findAll());
    }

    @Test
    void rejectsBlankNamesAndMissingUpdateId() {
        final var blank = new Trainee();
        blank.setFirstName(" ");
        blank.setLastName("Smith");
        assertThrows(IllegalArgumentException.class, () -> service.create(blank));
        verify(traineeDao, never()).save(any());

        final var missingId = new Trainee();
        missingId.setFirstName("John");
        missingId.setLastName("Smith");
        assertThrows(IllegalArgumentException.class, () -> service.update(missingId));
    }
}
