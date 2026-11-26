package com.epam.gym.service;

import com.epam.gym.dao.TrainingDao;
import com.epam.gym.model.Training;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {
    @Mock
    private TrainingDao trainingDao;

    @Test
    void createAndFindTraining() {
        final var service = new TrainingServiceImpl(trainingDao);
        final var training = new Training();
        training.setTrainingName("Cardio");
        when(trainingDao.save(any(Training.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(trainingDao.findById(5L)).thenReturn(Optional.of(training));

        final var created = service.create(training);
        final var found = service.findById(5L).orElseThrow();

        assertEquals(created, found);
    }

    @Test
    void findAllDelegatesToDaoAndNullCreateIsRejected() {
        final var service = new TrainingServiceImpl(trainingDao);
        final var training = new Training();
        when(trainingDao.findAll()).thenReturn(List.of(training));

        assertEquals(List.of(training), service.findAll());
        assertThrows(IllegalArgumentException.class, () -> service.create(null));
    }
}
